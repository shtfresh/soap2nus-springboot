FROM openjdk:12-oraclelinux7

RUN apk add maven \
    && mvn -v \
    && mkdir /app

COPY . /app/

# build the package
RUN cd /app \
    && mvn install:install-file -Dfile=lib/ojdbc8.jar -DgroupId=com.oracle.jdbc -DartifactId=ojdbc8 -Dversion=18.3 -Dpackaging=jar \
    && mvn clean package

EXPOSE 30010

WORKDIR /app

CMD ["java", "-jar", "target/service-oracle-0.0.1-SNAPSHOT.jar"]
