# GBTec Email Server

## Requirements

- Java 21 (e.g. Amazon Corretto)
- Maven (e.g. bundled by IntelliJ)
- Docker
- Api Client (e.g. Postman, [Bruno](https://www.usebruno.com/))
    - Collections can be found in the folder `api-collection/` 

## Start the project

Before starting the project locally, you need to start a local Docker Instance of the Postgres Database.  
Therefore, run the following command:

```shell
docker run --name gbtec-email-server -p 5432:5432 -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=test -e POSTGRES_DB=emails -d postgres:17.4
```

You can start the project by running the following command
```shell
mvn spring-boot:run
```
or just start the Spring Boot Application from the `de.beg.gbtec.emails.Application` file.

## Outlook
There 