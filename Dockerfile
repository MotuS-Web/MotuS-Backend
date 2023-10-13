FROM openjdk:11-jdk

# 앱 디렉토리 생성
WORKDIR /app

# 앱 종속성 복사
COPY build/libs/motus-0.0.1-SNAPSHOT.jar motus-0.0.1-SNAPSHOT.jar

# 앱 실행을 위한 사용자 계정 생성
RUN addgroup --system dockeruser && adduser --system --ingroup dockeruser dockeruser

# 사용자 계정으로 소유권 변경
RUN chown dockeruser:dockeruser motus-0.0.1-SNAPSHOT.jar

#사용자 계정으로 전환
USER dockeruser

#노출할 포트 명시
EXPOSE 8081

# 시스템 진입점 정의
ENTRYPOINT ["java","-jar","/app/motus-0.0.1-SNAPSHOT.jar"]