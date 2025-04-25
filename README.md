# GBTec Email Server - Implementation Documentation

## Overview
The GBTec Email Server is a Spring Boot application that provides a RESTful API for managing emails. It allows users to create, read, update, and delete emails, as well as perform bulk operations. The application also includes a scheduled task to automatically mark emails as spam based on known spam addresses.

## Technology Stack
- **Java 21**: The application is built using Java 21.
- **Spring Boot**: The application uses Spring Boot as its framework.
- **PostgreSQL**: The application uses PostgreSQL as its database.
- **Maven**: The application uses Maven for dependency management and building.

## Architecture
The application follows a layered architecture:
1. **Controller Layer**: Handles HTTP requests and responses.
2. **Service Layer**: Contains the business logic.
3. **Repository Layer**: Handles data access.

## Start the project local

### Requirements
- Java 21 (e.g. Amazon Corretto)
- Maven (e.g. bundled by IntelliJ)
- Docker
- Api Client (e.g. Postman, [Bruno](https://www.usebruno.com/))
  - Collections can be found in the folder `api-collection/`

Before starting the project, you need to start a local Docker Instance of the Postgres Database.
```shell
docker run --name gbtec-email-server -p 5432:5432 -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=test -e POSTGRES_DB=emails -d postgres:17.4
```

You can start the project by running the following command
```shell
mvn spring-boot:run
```
or just start the Spring Boot Application from the `de.beg.gbtec.emails.Application` file.

## Decision Records
- **Java 21**: When selecting the Java version, I would always opt for the latest LTS release at the time.
  These now contain many modern features and make working with Java even more interesting. In a commercial environment, however, you always have to keep an eye on stability, support and longevity. With an STS release, the time span until the next update is significantly shorter, which makes it necessary to update much more frequently. And often the new, interesting features are not yet included or are only included as a preview, so they are not reliable yet.
- **Spring Boot**: In my experience, Spring Boot is the most widely used solution for implementing a web application in a commercial Java environment and is therefore one of the main reasons why I would use it again and again. The barrier to entry is extremely low. You can find a sheer mass of tutorials and articles on the most important topics and, above all, other developers who can directly adopt Spring Boot productively.
  In addition, there are many dependencies with abstractions to connect the application to the infrastructure.
- **PostgreSQL**: I picked up PostgreSQL as my database solution for several reasons:
  - **SQL Standard**: PostgreSQL implements a significant portion of the SQL standard, providing a familiar query language that most developers already know. This reduces the learning curve and enables efficient onboarding of new team members.

  - **Extensibility**: PostgreSQL's architecture allows for significant extensibility through its extension system. Notable examples include PostGIS for geospatial data handling. This extensibility ensures the database can evolve with our application's needs.

  - **Cost Efficiency**: As an open-source solution, PostgreSQL has no licensing costs that would be incurred with commercial alternatives like Oracle or SQL Server. This provides substantial cost savings.

  - **Deployment Flexibility**: PostgreSQL can be hosted in virtually any environment, including all major cloud providers, containerized environments, or traditional on-premises servers. This flexibility prevents vendor lock-in and allows us to optimize our hosting strategy based on evolving requirements. 
  
  These factors make PostgreSQL an excellent choice for any application, providing a balance of performance, features, community support, and cost-effectiveness.