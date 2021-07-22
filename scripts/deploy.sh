#!/bin/bash
REPOSITORY=/home/ec2-user/app/step2
PROJECT_NAME=spring_aws_project

echo "> Build 파일 복사"

cd $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> 현재 구동 중인 애플리케이션 pid 확인"

#현재 실행중인 스프링 부트 애플리케이션 종료
CURRENT_PID=$(pgrep -fl sprint_aws_project | grep jar | awk '{print $1}')

echo "> 현재 구동 중인 애플리케이션pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
  echo "현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo ">8080 포트 강제 종료"

SHUT_PORT=$(netstat -tnlp | grep 8080 | tr -s ' ' ' ' | cut -d ' ' -f7 | cut -d '/' -f1)

if [ -z "SHUT_PORT" ]; then
	kill -9 $SHUT_PORT
fi

echo "> 새 애플리케이션 배포"

#새로 실행할 JAR 파일명 찾기
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME에 실행권한 추가"

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

nohup java -jar \
	-Dspring.config.location=classpath:/application.properties,classpath:/application-real.properties,/home/ec2-user/app/application-oauth.properties,/home/ec2-user/app/application-real-db.properties \
	-Dspring.profiles.active=real \
	$JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
