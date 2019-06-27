## Building our project

#### Installing Gradle

First of all you will need to install [Gradle Build Tool](https://gradle.org/install/) into your system.

#### Using Gradle to build the project

In order to start building the project with Gradle you need to move into the project's directory

```shell
cd yourPathTo/Tactical
```

And then run the shadowJar task from gradle using

```sh
gradle shadowJar
```

Then you can start the project from the root directory by using 

```shell
java -jar build/libs/Tactical-1.0-all.jar
```
