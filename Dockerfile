#FROM maven:3.8.1-openjdk-17-slim AS packager
#RUN mkdir /project
#COPY . /project
#WORKDIR /project
#RUN --mount=type=cache,target=/root/.m2,rw mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy AS builder
ARG JAR_FILE=/target/*.jar
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:17-jre-jammy
RUN mkdir /app
WORKDIR /app
COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./
RUN addgroup --system javauser && adduser --system --shell /bin/false --ingroup javauser javauser
RUN chown -R javauser:javauser /app
USER javauser
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]