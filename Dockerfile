#
# Build stage
#
FROM gradle:8.4-jdk17-alpine AS build
WORKDIR /home/app
COPY . /home/app
RUN gradle build --no-daemon

#
# Package stage
#
FROM openjdk:17-alpine
COPY --from=build /home/app/build/libs/instabox-bot-0.0.1-SNAPSHOT.jar /usr/local/lib/instabox-bot-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/instabox-bot-0.0.1-SNAPSHOT.jar"]
