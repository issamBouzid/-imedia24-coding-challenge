FROM openjdk:11
ARG SHOP_JAR_FILE
COPY ${SHOP_JAR_FILE} imedia24-shop-v1.jar
ENTRYPOINT ["java","-jar","/imedia24-shop-v1.jar"]