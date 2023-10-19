# Motus-Backend

<div align="center">
  <img src="https://github.com/MotuS-Web/MotuS-Backend/assets/80760160/dea1f252-ec63-410f-8516-fc4adcfd1393" alt="motus_logo" width="300" height="300">
</div>

## Descriptions

Backend uses JAVA to build an API server that communicates with the client as a Spring-Boot framework.

The main purpose of the API server is to store and manage the output received by the client through the AI-Server and the uploaded Guide Data (mp4 & Json) in the DB server to receive the program that the user wants.

This model will be able to function in all areas that collect and utilize human motor movements

## A Table of Contents
* [1. Installation](#1-installation)
  * [1.1. Git Clone](#11-git-clone)
  * [1.2. Configuration](#12-configuration)
    * [application.yml](#applicationyml)
  * [1.3. Docker ](#13-dockerfile)
  * [1.4. Build & Execute](#14-build--execute)
    * [1.4.1. Condition ](#141-condition)
    * [1.4.2. Local Environment](#142-local-environment)
    * [1.4.3. Docker Environment](#143-docker-environment)
* [2. Usage](#2-usage)
  * [2.1. Environment](#21-environment)
  * [2.2. Development](#22-development)
  * [2.3. Dependencies](#23-dependencies)
* [3. System Architecture](#3-system-architecture)
  * [3.1. Consumer Function Architecture](#31-consumer-function-architecture)
  * [3.2. Admin Function Architecture](#32-admin-function-architecture)
* [4. Documentation](#4-documentation)
  * [4.1. NCP Object Storage ](#41-ncp-object-storage-)
  * [4.2. Documentation2](#42-documentation-2)
* [5. Open source Licensing Info](#5-open-source-licensing-info)

## 1. Installation

### 1.1. Git Clone
`$ git clone https://github.com/MotuS-Web/MotuS-Backend.git`

### 1.2. Configuration
It needs to be modified to meet server specifications.

#### application.yml

```yml
spring:
  datasource:
    driver-class-name: org.mariadb.jdbc.Driver
    url: jdbc:mariadb://{host}:{port}/{dbname}
    username: {username}
    password: {password}

  jpa:
    properties:
      hibernate:
        format_sql: true
        show_sql: true
    defer-datasource-initialization : true
  mvc:
    pathmatch:
      matching-strategy: ant_path_matcher

  logging:
    config: classpath:log4j2.yml
  level:
    org.hibernate.SQL: debug

  servlet:
    multipart:
      max-file-size: 30MB
      max-request-size: 40MB

cloud:
  aws:
    credentials:
      access-key: {AWS_ACCESS_KEY_ID}
      secret-key: {AWS_SECRET_ACCESS_KEY}
    region:
      static: {your-region}
    s3:
      endpoint: https://kr.object.ncloudstorage.com
      bucket: {your-bucket}

```

### 1.3. Dockerfile
```dockerfile
FROM openjdk:11-jdk

# Create the app directory
WORKDIR /app

# Create a user account for running the app
RUN addgroup --system dockeruser && adduser --system --ingroup dockeruser dockeruser

# Change ownership of the /app folder to the new user account
RUN chown -R dockeruser:dockeruser /app

# Switch to the user account
USER dockeruser

# Specify the port to expose (default is 8080)
ARG APP_PORT=8080
EXPOSE ${APP_PORT}

# Copy and set up the app JAR file (default is app.jar)
ARG JAR_FILE=app.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]

```

### 1.4. Build & Execute

#### 1.4.1. Condition
- Database Connection
  - Check the database connection you use

- Object Storage Connection
  - Check the object storage connection of the Naver Cloud Platform(NCP).
  - Please refer to the [4.1. NCP Object Storage](#41-ncp-object-storage-)
#### 1.4.2. Local Environment
```shell
$ ./gradlew clean build -x test
$ cd build/libs
$ java -jar *.jar
```

#### 1.4.3. Docker environment
```shell
$ ./gradlew clean build -x test
$ docker build -t tag-name:1.0 .
$ docker run -p 8080:8081 -d --name=app-name tag-name:1.0
```

## 2. Usage
### 2.1. Environment
- InteliJ
- Postman
- Git Action
- Git
- Gradle
- Raspberry PI 4B

### 2.2. Development
- Spring-Boot
- Java
- Object Storage(Naver Cloud Platform)
- MariaDB

### 2.3. Dependencies
- QueryDsl
- Spring-Data-JPA
- Spring Security
- JWT(Json Web Token)
- WebSocket
- Lombok

## 3. System Architecture
### 3.1. Consumer Function Architecture
![image](https://github.com/Sirius506775/MotuS-Backend/assets/80760160/755656d9-76c9-4d24-b437-b2a76b19524d)

### 3.2. Admin Function Architecture
![image](https://github.com/Sirius506775/MotuS-Backend/assets/80760160/b589ad3d-1501-44c8-91b7-3ea9957aff7d)

## 4. Documentation
### 4.1. NCP Object Storage 
-  [ObjectStorageGuide_EN.md](docs/ObjectStorageGuide_EN.md)
-  [ObjectStorageGuide_KO.md](docs/ObjectStorageGuide_KO.md)

### 4.2. How to Contribute
-  [How To Contribute?](docs/CONTRIBUTING.md)

## 5. Open source licensing info
- [Apache License](LICENSE)
- [Third Party License](LICENSE_3rd.md)
