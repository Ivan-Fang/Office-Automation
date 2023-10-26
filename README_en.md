# Office Automation System

## Project Introduction

* This project is an office automation system for managing employee information and automating office processes.

* This project can be divided into two parts: the management side and the staff side.

    * The administration side is used to manage employee information, manage employee privileges, and manage office processes.
    
    * The staff side is responsible for allowing employees to apply for and approve office processes online.

* We adopt front-end and back-end separate mode to develop this project. The front-end page is based on `vue-admin-template`, and we mainly implements the "back-end" and "front and back-end API" part.

* The technologies used in this project are as follows:

    * Base framework: `SpringBoot`
    
    * Data manipulation: `Spring`, `Spring MVC`, `MyBatis`, `MyBatis Plus`

    * Database management system: `MySQL`

    * Data validation: `Spring Security`, `JWT` (Json Web Token)

    * Data caching: `Redis`

    * API file generation: `Knife4j`

    * Workflow engine: `Activiti`

    * Dependency Management: `Maven`

    * Front-end frameworks: `Vue`, `Element UI`, `Axios`

* This project is the Course Project of AtGuiGu ["雲尚辦公"](https://www.bilibili.com/video/BV1oM41177Jd/).

## Technical Details

We use the following technologies to complete this project:

* We use `SpringBoot`, `Spring`, `Spring MVC`, `MyBatis`, `MyBatis Plus` to implement CRUD manupulations of user, role, menu (permission), process type, and process template, as well as assigning roles and permissions.

* We use `Activiti` for workflow definition, deployment, activation, as well as task query and approval.

* We use `JWT` to encrypt user login information.

* We use `Spring Security` for login authorization and authentication.
    
* We use `Redis` to cache usernames and their permissions for authorization verification.

* We design front-end and back-end APIs using RESTful API style.

* We use `Knife4j` to generate backend API files.

## How to use

1. Start the project

    * Execute all the commands in `ivanfang-oa.sql` to create database and tables.

    * Run the backend program (note that this project is developed using Java 8, please make sure that the execution environment is Java 8 as well).
      ```
      cd ivanfang-oa-parent/service-oa/target
      java -jar service-oa.jar
      ```

    * Run the front-end program (administration side)
      ```
      cd ivanfang-oa/target java -jar service-oa.jar
      cd ivanfang-oa-admin
      npm install
      npm run dev
      ```

    * Run the front-end program (staff side)
      ```
      cd ivanfang-oa-web
      npm install
      npm run serve
      ```
    
    * Start redis-server and create a database with ip:port 127.0.0.1:6379.

2. Using the project

    * Using the administration side
      ```
      http://localhost:9528
      ```

    * Using the staff side
      ```
      http://localhost:9090/#/test
      ```
    
    * Viewing Backend API Documentation
      ```
      http://localhost:8805/doc.html
      ```

## Folder Description

* /ivanfang-oa-parent: backend code

* /ivanfang-oa-admin: front-end code - administration side

* /ivanfang-oa-web: front-end code - staff side
