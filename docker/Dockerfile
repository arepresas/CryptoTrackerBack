FROM maven:3.9.15-eclipse-temurin-25-noble AS build
WORKDIR /workspace

COPY pom.xml ./
RUN mvn -B -ntp dependency:go-offline

COPY src ./src
RUN mvn -B -ntp clean package -DskipTests

FROM gcr.io/distroless/java25-debian13:nonroot
COPY --from=build /workspace/target/*.jar /app/app.jar

EXPOSE 8700
CMD ["/app/app.jar"]
