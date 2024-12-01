# Task Management - banquemisr.challenge05

- [Task Managment Backend](#project-title)
  - [Description](#description)
  <!-- - [Team](#team) -->
  - [Technology](#technology)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installing](#installing)
  - [Testing](#test)
  - [Hint](#hint)



---

## Description

This is the backend application that provides web services for the task management.

---

## Technology

This backend application is built using Spring Boot Application along with Postgres database.

---

# Getting Started

## Prerequisites

Things you need to install the software and how to install them.

- [JDK 11](https://www.oracle.com/java/technologies/downloads/#java11)
- [Gradle 7.5](https://gradle.org/install/)

## Installing

Change database settings in application-default.yml

Run

```
./gradlew clean
```

then

```
./gradlew build
```

then

```
./gradlew bootRun

`````
## Testing
````
./gradlew test

````
## Hint
Added two user and  three tasks at database by default 
 at database by default 
- username: eslamadel , password: 1234 -> role is ADMIN
- username: eslamadel676 , password: 1234 -> role is REQULAR_USER
- anyone can login by any user
- anyone can import Task Management.postman_collection.json to postman to can test project.

  


