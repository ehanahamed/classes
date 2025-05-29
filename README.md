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

### Production Setup

So make like a user, if you already cloned the repository re-clone it under that user's home dir, `/home/ehclasses`
```
# adduser ehclasses
```

Build the WAR file
```
cd /home/ehclasses/classes
./gradlew build
```

Copy the systemd service
```
# cp ehclasses.service /etc/systemd/system/ehclasses.service
```

Reload systemd
```
sudo systemctl daemon-reload
```



