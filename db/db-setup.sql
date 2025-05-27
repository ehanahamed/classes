/*
This is similar to Quizfreely's database setup 

For use with Quizfreely, use Quizfreely's config/db/quizfreely-db-setup.sql instead
*/

create extension if not exists pgcrypto;
create extension if not exists pg_trgm;

create role quizfreely_api noinherit login;
create role eh_classes_api noinherit login;

create schema auth;

grant usage on schema auth to quizfreely_api;
grant usage on schema auth to eh_classes_api;

/*create function auth.get_user_id() returns uuid
language sql
as $$
select current_setting('quizfreely_authed_user.user_id')::uuid
$$;*/

create type auth_type_enum as enum ('username_password', 'oauth_google');
create table auth.users (
  id uuid primary key default gen_random_uuid(),
  username text,
  encrypted_password text,
  display_name text not null,
  auth_type auth_type_enum not null,
  oauth_google_id text,
  oauth_google_email text,
  unique (username),
  unique (oauth_google_id)
);

grant select on auth.users to quizfreely_api;
grant insert on auth.users to quizfreely_api;
grant update on auth.users to quizfreely_api;
grant delete on auth.users to quizfreely_api;
grant select on auth.users to eh_classes_api;

alter table auth.users enable row level security;

create policy select_users on auth.users
as permissive
for select
to quizfreely_api
using (
  ((select current_setting('qzfr_api.scope', true)) = 'auth') or (
    (select current_setting('qzfr_api.scope', true)) = 'user' and
    (select current_setting('qzfr_api.user_id', true))::uuid = id
  )
);

create policy select_users_eh_classes on auth.users
as permissive
to eh_classes_api
using (true);

create policy insert_users on auth.users
as permissive
for insert
to quizfreely_api
with check (
  (select current_setting('qzfr_api.scope', true)) = 'auth'
);

create policy update_users on auth.users
as permissive
for update
to quizfreely_api
using (
  ((select current_setting('qzfr_api.scope', true)) = 'auth') or (
    (select current_setting('qzfr_api.scope', true)) = 'user' and
    (select current_setting('qzfr_api.user_id', true))::uuid = id
  )
)
with check (true);

create policy delete_users on auth.users
as permissive
for delete
to quizfreely_api
using (
  (select current_setting('qzfr_api.scope', true)) = 'auth'
);

create view public.profiles as select
id, username, display_name from auth.users;

grant select on public.profiles to quizfreely_api;
grant select on public.profiles to eh_classes_api;

create table auth.sessions (
  token text primary key default encode(gen_random_bytes(32), 'base64'),
  user_id uuid not null,
  expire_at timestamptz default now() + '10 days'::interval
);

grant select on auth.sessions to quizfreely_api;
grant insert on auth.sessions to quizfreely_api;
grant update on auth.sessions to quizfreely_api;
grant delete on auth.sessions to quizfreely_api;
grant select on auth.sessions to eh_classes_api;

alter table auth.sessions enable row level security;

create policy select_sessions on auth.sessions
as permissive
for select
to quizfreely_api
using (
  (
    (select current_setting('qzfr_api.scope', true)) = 'auth'
  ) or (
    (select current_setting('qzfr_api.scope', true)) = 'user' and
    (select current_setting('qzfr_api.user_id', true))::uuid = user_id
  )
);

create policy select_sessions_eh_classes on auth.sessions
as permissive
for select
to eh_classes_api
using (true);

create policy insert_sessions on auth.sessions
as permissive
for insert
to quizfreely_api
with check (
  (select current_setting('qzfr_api.scope', true)) = 'auth'
);

create policy update_sessions on auth.sessions
as permissive
for update
to quizfreely_api
using (
  (
    (select current_setting('qzfr_api.scope', true)) = 'auth'
  ) or (
    (select current_setting('qzfr_api.scope', true)) = 'user' and
    (select current_setting('qzfr_api.user_id', true))::uuid = user_id
  )
)
with check (true);

create policy delete_sessions on auth.sessions
as permissive
for delete
to quizfreely_api
using (
  (
    (select current_setting('qzfr_api.scope', true)) = 'auth'
  ) or (
    (select current_setting('qzfr_api.scope', true)) = 'user' and
    (select current_setting('qzfr_api.user_id', true))::uuid = user_id
  ) or (
    expire_at < (select now())
  )
);

/*
  usage:
  select user_id from auth.verify_session('token goes here');
  if that returns 0 rows, session is invalid
  if that returns 1 row, session is valid, and user's id is returned user_id
*/
create function auth.verify_session(session_token text)
returns table(user_id uuid)
language sql
as $$
select user_id from auth.sessions where token = $1 and expire_at > (select now())
$$;

create procedure auth.delete_expired_sessions()
language sql
as $$
delete from auth.sessions where expire_at < (select now())
$$;

create table public.studysets (
  id uuid primary key default gen_random_uuid(),
  user_id uuid references auth.users (id) on delete set null,
  title text not null,
  private boolean not null,
  data jsonb not null,
  updated_at timestamptz default now(),
  terms_count int,
  featured boolean default false,
  tsvector_title tsvector generated always as (to_tsvector('english', title)) stored
);

create index textsearch_title_idx on public.studysets using GIN (tsvector_title);

grant select on public.studysets to quizfreely_api;
grant insert on public.studysets to quizfreely_api;
grant update on public.studysets to quizfreely_api;
grant delete on public.studysets to quizfreely_api;

alter table public.studysets enable row level security;

create policy select_studysets on public.studysets
as permissive
for select
to quizfreely_api
using (
  (private = false) or (
    (select current_setting('qzfr_api.scope', true)) = 'user' and
    (select current_setting('qzfr_api.user_id', true))::uuid = user_id
  )
);

create policy insert_studysets on public.studysets
as permissive
for insert
to quizfreely_api
with check (
  (select current_setting('qzfr_api.scope', true)) = 'user' and
  (select current_setting('qzfr_api.user_id', true))::uuid = user_id
);

create policy update_studysets on public.studysets
as permissive
for update
to quizfreely_api
using (
  (select current_setting('qzfr_api.scope', true)) = 'user' and
  (select current_setting('qzfr_api.user_id', true))::uuid = user_id
)
with check (true);

create policy delete_studysets on public.studysets
as permissive
for delete
to quizfreely_api
using (
  (select current_setting('qzfr_api.scope', true)) = 'user' and
  (select current_setting('qzfr_api.user_id', true))::uuid = user_id
);

create table public.studyset_progress (
  id uuid primary key default gen_random_uuid(),
  studyset_id uuid references public.studysets (id) on delete cascade,
  user_id uuid references auth.users (id) on delete cascade,
  terms jsonb not null,
  updated_at timestamptz default now()
);

grant select on public.studyset_progress to quizfreely_api;
grant insert on public.studyset_progress to quizfreely_api;
grant update on public.studyset_progress to quizfreely_api;
grant delete on public.studyset_progress to quizfreely_api;

alter table public.studyset_progress enable row level security;

create policy select_studyset_progress on public.studyset_progress
as permissive
for select
to quizfreely_api
using (
  (select current_setting('qzfr_api.scope', true)) = 'user' and
  (select current_setting('qzfr_api.user_id', true))::uuid = user_id
);

create policy insert_studyset_progress on public.studyset_progress
as permissive
for insert
to quizfreely_api
with check (
  (select current_setting('qzfr_api.scope', true)) = 'user' and
  (select current_setting('qzfr_api.user_id', true))::uuid = user_id
);

create policy update_studyset_progress on public.studyset_progress
as permissive
for update
to quizfreely_api
using (
  (select current_setting('qzfr_api.scope', true)) = 'user' and
  (select current_setting('qzfr_api.user_id', true))::uuid = user_id
)
with check (true);

create policy delete_studyset_progress on public.studyset_progress
as permissive
for delete
to quizfreely_api
using (
  (select current_setting('qzfr_api.scope', true)) = 'user' and
  (select current_setting('qzfr_api.user_id', true))::uuid = user_id
);

create table public.search_queries (
  query text primary key,
  subject text
);

grant select on public.search_queries to quizfreely_api;
alter table public.search_queries disable row level security;

create table public.studyset_settings (
  id uuid primary key default gen_random_uuid(),
  studyset_id uuid references public.studysets (id) on delete cascade,
  user_id uuid references auth.users (id) on delete cascade,
  settings jsonb not null,
  updated_at timestamptz default now()
);

grant select on public.studyset_settings to quizfreely_api;
grant insert on public.studyset_settings to quizfreely_api;
grant update on public.studyset_settings to quizfreely_api;
grant delete on public.studyset_settings to quizfreely_api;

alter table public.studyset_settings enable row level security;

create policy select_studyset_settings on public.studyset_settings
as permissive
for select
to quizfreely_api
using (
  (select current_setting('qzfr_api.scope', true)) = 'user' and
  (select current_setting('qzfr_api.user_id', true))::uuid = user_id
);

create policy insert_studyset_settings on public.studyset_settings
as permissive
for insert
to quizfreely_api
with check (
  (select current_setting('qzfr_api.scope', true)) = 'user' and
  (select current_setting('qzfr_api.user_id', true))::uuid = user_id
);

create policy update_studyset_settings on public.studyset_settings
as permissive
for update
to quizfreely_api
using (
  (select current_setting('qzfr_api.scope', true)) = 'user' and
  (select current_setting('qzfr_api.user_id', true))::uuid = user_id
)
with check (true);

create policy delete_studyset_settings on public.studyset_settings
as permissive
for delete
to quizfreely_api
using (
  (select current_setting('qzfr_api.scope', true)) = 'user' and
  (select current_setting('qzfr_api.user_id', true))::uuid = user_id
);

create schema classes;

grant usage on schema classes to eh_classes_api;

create table classes.courses (
  id bigserial primary key,
  name text not null
);

grant select on classes.courses to eh_classes_api;
grant insert on classes.courses to eh_classes_api;
grant update on classes.courses to eh_classes_api;
grant delete on classes.courses to eh_classes_api;
grant usage, select on sequence classes.courses_id_seq to eh_classes_api;

create table classes.course_authors (
    course_id bigint references classes.courses (id) on delete cascade,
    author_user_id uuid references auth.users (id) on delete cascade,
    primary key (course_id, author_user_id)
);

grant select on classes.course_authors to eh_classes_api;
grant insert on classes.course_authors to eh_classes_api;
grant update on classes.course_authors to eh_classes_api;
grant delete on classes.course_authors to eh_classes_api;

create table classes.course_viewers (
    course_id bigint references classes.courses (id) on delete cascade,
    user_id uuid references auth.users (id) on delete cascade,
    primary key (course_id, user_id)
);

grant select on classes.course_viewers to eh_classes_api;
grant insert on classes.course_viewers to eh_classes_api;
grant update on classes.course_viewers to eh_classes_api;
grant delete on classes.course_viewers to eh_classes_api;

create table classes.classes (
  id bigserial primary key,
  name text not null,
  course_id bigint references classes.courses (id) on delete set null,
  color text
);

grant select on classes.classes to eh_classes_api;
grant insert on classes.classes to eh_classes_api;
grant update on classes.classes to eh_classes_api;
grant delete on classes.classes to eh_classes_api;
grant usage, select on sequence classes.classes_id_seq to eh_classes_api;

create table classes.classes_students (
    class_id bigint references classes.classes (id) on delete cascade,
    student_user_id uuid references auth.users (id) on delete cascade,
    primary key (class_id, student_user_id)
);

grant select on classes.classes_students to eh_classes_api;
grant insert on classes.classes_students to eh_classes_api;
grant update on classes.classes_students to eh_classes_api;
grant delete on classes.classes_students to eh_classes_api;

create table classes.classes_teachers (
    class_id bigint references classes.classes (id) on delete cascade,
    teacher_user_id uuid references auth.users (id) on delete cascade,
    primary key (class_id, teacher_user_id)
);

grant select on classes.classes_teachers to eh_classes_api;
grant insert on classes.classes_teachers to eh_classes_api;
grant update on classes.classes_teachers to eh_classes_api;
grant delete on classes.classes_teachers to eh_classes_api;

create table classes.assignments (
    id bigserial primary key,
    class_id bigint references classes.classes (id) on delete cascade,
    teacher_id uuid references auth.users (id) on delete set null,
    title text not null,
    description_prosemirror_json jsonb,
    points smallint,
    due_at timestamptz,
    created_at timestamptz default now(),
    updated_at timestamptz default now()
);

grant select on classes.assignments to eh_classes_api;
grant insert on classes.assignments to eh_classes_api;
grant update on classes.assignments to eh_classes_api;
grant delete on classes.assignments to eh_classes_api;
grant usage, select on sequence classes.assignments_id_seq to eh_classes_api;

create table classes.assignment_drafts (
    id bigserial primary key,
    class_id bigint references classes.classes (id) on delete cascade,
    teacher_id uuid references auth.users (id) on delete set null,
    title text not null,
    description_prosemirror_json jsonb,
    points smallint,
    due_at timestamptz,
    created_at timestamptz default now(),
    updated_at timestamptz default now()
);

grant select on classes.assignment_drafts to eh_classes_api;
grant insert on classes.assignment_drafts to eh_classes_api;
grant update on classes.assignment_drafts to eh_classes_api;
grant delete on classes.assignment_drafts to eh_classes_api;
grant usage, select on sequence classes.assignment_drafts_id_seq to eh_classes_api;

create table classes.assignment_submissions (
    id bigserial primary key,
    assignment_id bigint references classes.assignments (id) on delete cascade,
    attachments jsonb,
    submitted boolean,
    submitted_at timestamptz,
    pointsEarned smallint
);

grant select on classes.assignment_submissions to eh_classes_api;
grant insert on classes.assignment_submissions to eh_classes_api;
grant update on classes.assignment_submissions to eh_classes_api;
grant delete on classes.assignment_submissions to eh_classes_api;
grant usage, select on sequence classes.assignment_submissions_id_seq to eh_classes_api;

create type submission_action_type as enum ('submit', 'unsubmit', 'add_grade', 'update_grade', 'remove_grade', 'add_attachment', 'update_attachment', 'remove_attachment');

create table classes.assignment_submission_history (
    id bigserial primary key,
    assignment_submission_id bigint references classes.assignment_submissions (id) on delete cascade,
    action_type submission_action_type not null,
    timestamp timestamptz not null
);

grant select on classes.assignment_submission_history to eh_classes_api;
grant insert on classes.assignment_submission_history to eh_classes_api;
grant usage, select on sequence classes.assignment_submission_history_id_seq to eh_classes_api;

create table classes.class_user_settings (
    class_id bigint references classes.classes (id) on delete cascade,
    user_id uuid references auth.users (id) on delete cascade,
    color text,
    primary key (class_id, user_id)
);

grant select on classes.class_user_settings to eh_classes_api;
grant insert on classes.class_user_settings to eh_classes_api;
grant update on classes.class_user_settings to eh_classes_api;
grant delete on classes.class_user_settings to eh_classes_api;

create table classes.announcements (
    id bigserial primary key,
    user_id uuid references auth.users (id) on delete cascade,
    class_id bigint references classes.classes (id) on delete cascade,
    content_prosemirror_json jsonb,
    created_at timestamptz default now(),
    updated_at timestamptz default now()
);

grant select on classes.announcements to eh_classes_api;
grant insert on classes.announcements to eh_classes_api;
grant update on classes.announcements to eh_classes_api;
grant delete on classes.announcements to eh_classes_api;
grant usage, select on sequence classes.announcements_id_seq to eh_classes_api;

create table classes.class_codes (
    code text primary key,
    class_id bigint references classes.classes (id) on delete cascade,
    created_at timestamptz default now()
);

grant select on classes.class_codes to eh_classes_api;
grant insert on classes.class_codes to eh_classes_api;
grant update on classes.class_codes to eh_classes_api;
grant delete on classes.class_codes to eh_classes_api;

