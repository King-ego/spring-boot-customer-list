FROM openjdk:21-jdk-slim

RUN apt-get update && apt-get install -y wget

WORKDIR /src/app

COPY . .

EXPOSE 8079

CMD ["./mvnw", "spring-boot:run"]
