FROM maven:3.8.5-jdk-11-slim

WORKDIR auth-microservice

COPY . .

RUN mkdir -p /odeyalo/dev/microservices/auth/qrcodes/

ENTRYPOINT ["mvn", "spring-boot:run"]
