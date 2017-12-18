# CodingWeek 2017 - Sample project

This project provides a sample application that demonstrates the use of [Gradle](https://gradle.org/) for building a JavaFX application.

It provides basic dependencies such as [Log4j2](https://logging.apache.org/log4j/2.x/) for logging messages, [JUnit](http://junit.org/junit4/) for unit testing and [GitLab API for Java](https://github.com/gmessner/gitlab4j-api) for conversing with a GitLab server.

All these dependencies are defined in the `build.gradle` file. You can freely edit this file in order to add your own dependencies or replace GitLab API for Java library with [Java GitLab API](https://github.com/timols/java-gitlab-api) (by uncommenting `org.gitlab:java-gitlab-api:1.2.8`)

## Basic Gradle commands

Gradle will help you to build and distribute your project. It will allow you to easily manage your dependencies.

Gradle supports the following basic tasks on this project:
- `gradle test` to run the unit tests
- `gradle run` to run the project as a Java application
- `gradle assembleDist` to bundle the project as distribution in a `.zip` and a `.tar` files.
- `gradle clean` to clean up the project (deletes the `build` and `dist` directories)
- `gradle tasks` to list all the available tasks

**Note**: if `gradle` is not installed on your system, you can use the provided wrapper by using the `gradlew` command instead of the `gradle` one.

## Importing this project configuration in an IDE

Gradle is supported in mainstream IDEs such as IntelliJ, Eclipse or NetBeans. Therefore, you can easily import this project in your favorite IDE without having to reconfigure your build process.

### IntelliJ

IntelliJ allows to import gradle project. You just have to import the project and select the `build.gradle` file during the importation.

### Eclipse

Eclipse allows to import a gradle project.

### NetBeans

You first have to install the gradle plugin in NetBeans before being able to import a gradle project.
