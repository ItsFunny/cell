# First stage: complete build environment
FROM kevinyan001/aliyun-mvn:0.0.1 AS builder

# add pom.xml and source code
ADD ./pom.xml pom.xml
ADD ./src src/

# package jar
RUN mvn clean package  -Dmaven.test.skip=true --quiet

# Second stage: minimal runtime environment
From openjdk:8-jre-alpine

# copy jar from the first stage
COPY --from=builder target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]