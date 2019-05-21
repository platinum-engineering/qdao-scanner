# Gradle build
FROM gradle:jdk10 as builder

COPY --chown=gradle:gradle ./ /home/gradle/src
WORKDIR /home/gradle/src

RUN gradle dist
#RUN rm -rf /home/gradle/src/dist/log4j-slf4j-impl-2.11.2.jar

# Application
FROM openjdk:8-jre-slim

MAINTAINER Nikolay <contact@nskrash.ru>

EXPOSE 8000

COPY --from=builder /home/gradle/src/dist/ /app/
WORKDIR /app

ENTRYPOINT ["/usr/bin/java"]
CMD ["-cp", "./*", "io.qdao.scanner.QDabScannerRunner"]