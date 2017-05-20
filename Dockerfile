FROM ubuntu:16.04
MAINTAINER happylifeplat
VOLUME /tmp
EXPOSE 8093
ENV TZ=Asia/Shanghai
ENV PATH=$PATH:/java/jre/bin
ENV APP_NAME=happylife-service-search
ENV JAR_PATH=/happylife-service-search.jar
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
RUN mkdir -p /logs/
ADD happylife-service-search/target/happylife-service-search.jar $JAR_PATH
CMD java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar $JAR_PATH