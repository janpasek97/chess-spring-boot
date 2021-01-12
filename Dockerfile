FROM tomcat:latest
ADD target/FiveInARow.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
