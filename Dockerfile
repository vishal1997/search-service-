ARG APP_PORT=8088
ARG ADMIN_PORT=8089
ARG MAIN_CLASS="com.youtube.search.SearchApplication"
FROM openjdk:8-jdk-alpine

#RUN addgroup -S appgroup -g 31111 && adduser -S appuser -u 31112 -g 31111 appgroup

ARG USER_ID=1000
ARG GROUP_ID=1000
RUN addgroup -g ${GROUP_ID} mygroup \
 && adduser -D myuser -u ${USER_ID} -g myuser -G mygroup -s /bin/sh -h /

RUN mkdir -p search-service/lib
RUN chown myuser:mygroup search-service/
USER myuser

COPY --chown=myuser:mygroup target/search-api-1.0-SNAPSHOT.jar search-service/
COPY --chown=myuser:mygroup target/lib/ search-service/lib
COPY --chown=myuser:mygroup src/main/resources/config.yaml search-service/

COPY --chown=myuser:mygroup start search-service/

RUN ["chmod", "+x", "search-service/start"]
EXPOSE $APP_PORT $ADMIN_PORT
#RUN java  -cp search-service/lib/*:search-api-1.0-SNAPSHOT.jar com.youtube.search.SearchApplication server search-service/config.yaml

ENTRYPOINT ["sh", "-c", "search-service/start"]