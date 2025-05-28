# Classes

A classroom management tool and assignment tracker made to help teachers and students assign, submit, organize, and share assignments and studying resources

## Setup

These instructions are for running the server process.

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
