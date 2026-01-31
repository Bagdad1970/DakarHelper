FROM maven:3.9.12 as DEPS

WORKDIR /app

COPY pom.xml .
COPY Models/pom.xml Models/pom.xml
COPY EmailHandler/pom.xml EmailHandler/pom.xml
COPY ExcelParser/pom.xml ExcelParser/pom.xml
COPY DakarHelperService/pom.xml DakarHelperService/pom.xml

RUN mvn -B -e -C org.apache.maven.plugins:maven-dependency-plugin:3.9.0:go-offline

FROM maven:3.9.12 as BUILDER

WORKDIR /app

COPY --from=deps /root/.m2 /root/.m2
COPY --from=deps /app/ /app

COPY Models/src /app/Models/src
COPY EmailHandler/src /app/EmailHandler/src
COPY ExcelParser/src /app/ExcelParser/src
COPY DakarHelperService/src /app/DakarHelperService/src

RUN mvn -B -e -o clean install -DskipTests=true

FROM eclipse-temurin:24

WORKDIR /app

RUN mkdir /app/data

COPY --from=builder /app/DakarHelperService/target/DakarHelperService-0.0.1-SNAPSHOT.jar .

CMD [ "java", "-jar", "/app/DakarHelperService-0.0.1-SNAPSHOT.jar" ]