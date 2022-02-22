FROM openjdk:16-jdk-alpine
# FROM은 이미지 빌드시 기반으로 사용하는 것을 호출하는 커멘드이다. 현재는 spring을 빌드하는데 사용하는 JDK 버전을 호출한다.

# ARG JAR_FILE=build/libs/*.jar
# ARG 빌드시 사용할 환경 변수를 선언하는데 여기서는 Spring jar 파일이 생성되는 위치를 변수로 선언하였다.


COPY ./build/libs/*.jar ./app.jar
# Spring jar 파일을 복사하여 app.jar 변수를 생성하였다. 그 이유는 spring jar의 이름에 상관없이 하기 위해서 app.jar로 통일 하여 이미지를 빌드하기 위해서이다.

ENTRYPOINT ["java","-jar","/app.jar"]
# ENTRYPOINT는 이미지를 컨테이너화 시킬 때 Spring이 구동을 자동화 시키기 위한 shell 스크립트이다.ENTRYPOINT


