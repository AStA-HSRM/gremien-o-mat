FROM tomcat:9-jre11

# RUN rm -rf /usr/local/tomcat/webapps/ROOT
COPY ./build/libs/gremiomat-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/gremiomat.war
# CMD ["catalina.sh","run"]