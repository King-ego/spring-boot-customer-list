FROM openjdk:21-jdk-slim

WORKDIR /src/app

COPY . .

EXPOSE 8079

CMD ["./mvnw", "spring-boot:run"]
