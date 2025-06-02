# Classes

Classes is a classroom management tool and assignment tracker. It helps teachers and students assign, submit, organize, and share assignments and studying resources.

https://quizfreely.org/classes

[Codeberg](https://codeberg.org/ehanahamed/classes) · [GitHub](https://github.com/ehanahamed/classes)

## Setup

These instructions are for developers running the server process. Users do not need to install or setup anything, just go to https://quizfreely.org/classes

Clone the repository:
```sh
git clone https://codeberg.org/ehanahamed/classes
# or
# git clone https://github.com/ehanahamed/classes
```

Copy `src/main/resources/application-example.properties` to `src/main/resources/application.properties` and set variables/properties to connect to the database and configure the API.
```sh
$ cp src/main/resources/application-example.properties src/main/resources/application.properties
```


Start the server with `gradlew`:
```sh
$ ./gradlew bootRun
```

### Production Setup

So make like a user, if you already cloned the repository re-clone it under that user's home dir, `/home/ehclasses`
```
# adduser ehclasses
```

Change `src/main/resources/application.properties` before building the WAR file.

Use environment variables to set stuff outside of the WAR, and add those environment variables in `.env`.

While compiling/building, we need to manually `source` the `.env` file, but after that, systemd will easily use it automatically-ish (it's set in `EnvironmentFile` in `ehclasses.service`.

Build the WAR file
```
cd /home/ehclasses/classes
source .env # load the env file while building
./gradlew build
cp /build/libs/classes-VERSION.war /build/libs/classes.war
```

Copy the systemd service
```
# cp ehclasses.service /etc/systemd/system/ehclasses.service
```

Reload systemd
```
# systemctl daemon-reload
```

Start the service/process
```
# systemctl start ehclasses
```

