## Authorization and authentication microservice using Spring Eureka

### General info: 
Authenticate and autorize user, contains users and user roles

## Technologies used:
```
$ Java
$ Spring(Core, MVC REST, Data, Eureka Cloud)
$ Postgresql
$ Docker
$ Apache Kafka
```
[local.env](./local.env)

## Setup
  To run this microservice local you need:
  ```
  $ Clone this repo
  $ Update [local.env](./local.env) file. Add to this file your JWT_TOKEN_SECRET_KEY, GOOLGE_CLIENT_ID and GOOLGE_CLIENT_SECRET.
  $ docker-compose up -d
  ```
After following these 3 steps you will have a working service, that you can easy send requests to localhost:8090.
