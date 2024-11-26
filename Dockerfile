FROM maven:3-eclipse-temurin-21-alpine AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY . .
RUN mvn clean package -DskipTests

# Extract the application dependencies
RUN jar xf target/jlinkdemo-0.0.1-SNAPSHOT.jar 

RUN jdeps --ignore-missing-deps -q  \
    --recursive  \
    --multi-release 21  \
    --print-module-deps  \
    --class-path $(java -jar target/jlinkdemo-0.0.1-SNAPSHOT.jar --thin.classpath)  \
    target/jlinkdemo-0.0.1-SNAPSHOT.jar > deps.info


# Create the custom JRE
RUN jlink \
    --verbose \
    --add-modules $(cat deps.info) \
    --strip-debug \
    --compress 2 \
    --no-header-files \
    --no-man-pages \
    --output /custom_jre 

RUN java -jar target/jlinkdemo-0.0.1-SNAPSHOT.jar --thin.root=m2 --thin.dryrun

RUN java -jar target/jlinkdemo-0.0.1-SNAPSHOT.jar --thin.classpath=properties > thin.properties

FROM alpine:3.20

ENV JAVA_HOME=/opt/java/openjdk
ENV PATH "${JAVA_HOME}/bin:${PATH}"
COPY --from=build /custom_jre $JAVA_HOME

WORKDIR /opt/jlinkdemo/
COPY --from=build /build/thin.properties /root
COPY --from=build /build/deps.info /root
COPY --from=build /build/m2 /m2
COPY --from=build /build/target/jlinkdemo-0.0.1-SNAPSHOT.jar jlinkdemo.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "jlinkdemo.jar" ,"--thin.root=/m2" ]
