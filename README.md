# Motus-Backend

<div align="center">
  <img src="https://github.com/MotuS-Web/MotuS-Backend/assets/80760160/93089974-9c15-4428-926d-0c940464c39d" alt="motus_logo" width="300" height="300">
</div>


## Descriptions

Backend uses JAVA to build an API server that communicates with the client as a Spring-Boot framework.

The main purpose of the API server is to store and manage the output received by the client through the AI-Server and the uploaded Guide Data (mp4 & Json) in the DB server to receive the program that the user wants.

This model will be able to function in all areas that collect and utilize human motor movements

## A Table of Contents
* [1. Installation](#1-installation)
  * [1.1 Git Clone](#11-git-clone)
  * [1.2 Configuration](#12-configuration)
    * [application.yml](#applicationyml)
  * [1.3 Build](#13-build)
* [2. Usage](#2-usage)
  * [2.1. Environment](#21-environment)
  * [2.2. Development](#22-development)
* [3. How to test the software](#3-how-to-test-the-software)
* [4. System Architecture](#4-system-architecture)
* [5. Getting help](#5-getting-help)
* [6. Documentation](#6-documentation)
  * [6.1 NCP Object Storage ](#61-ncp-object-storage-)
  * [6.2 Documentation2](#62-documentation-2)
* [Open source licensing info](#open-source-licensing-info)
* [References](#references)

## 1. Installation

### 1.1 Git Clone
`$ git clone https://github.com/MotuS-Web/MotuS-Backend.git`

### 1.2 Configuration
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

server:
  ssl:
    key-store: key-store-path
    key-store-type: PKCS12
    key-store-password: 1111


```
### 1.3 Build
```shell
$ ./gradlew clean build -x test
$ cd build/libs
$ java -jar *.jar
```

## 2. Usage
#### 2.1. Environment
- InteliJ
- Postman
- Git Action
- Git
- Gradle

#### 2.2. Development
- Spring-Boot
- Java
- NCP(Naver Cloud Platform)
- MariaDB
- .... writing...ETC

## 3. How to test the software
If the software includes automated tests, detail how to run those tests.

## 4. System Architecture


## 5. Getting help
#### Example
If you have questions, concerns, bug reports, etc, please file an issue in this repository's Issue Tracker.

## 6. Documentation
### 6.1 NCP Object Storage 
-  [ObjectStorageGuide_EN.md](ObjectStorageGuide_EN.md)
-  [ObjectStorageGuide_KO.md](ObjectStorageGuide_KO.md)
### 6.2 Documentation 2


---
## Open source licensing info
1. [License](http://www.opensource.org/licenses/mit-license)
2. 

## References
1. asdf
2. asdf
