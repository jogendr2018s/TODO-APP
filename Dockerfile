FROM openjdk:22-jdk
ADD target/todoapp.jar todoapp.jar
ADD /src/main/resources/wait-for-it.sh wait-for-it.sh
ADD config.yml local_properties.yaml
ENTRYPOINT ["./wait-for-it.sh", "db:3306", "--","java","-jar","/todoapp.jar","server","/local_properties.yaml"]
