# ReHab-Backend


### 1.  usage skills
#### 1.1. Environment
- InteliJ
- Postman
- Git Action
- Git
- Gradle
- Raspberry FI 4B

#### 1.2 Development
- Spring-Boot
- Java
- NCP(Naver Cloud Platform)
- MariaDB
  
#### 1.3 Dependencies
- QueryDsl
- Spring-Data-JPA
- Lombok

---
### 2. Descriptions

본 대회에서 Backend 개발 언어는 JAVA를 사용하며, Spring-Boot 프레임워크로 Client와 통신하는 API 서버를 구축한다. 

API 서버의 주 목적은 Client가 AI-Server를 통해 전달 받은 output과 업로드한 Guide Data(mp4 & Json)를 DB 서버에 저장하고, 관리하여 사용자가 원하는 프로그램을 제공 받는 데에 있다.

주요 기능 정보는 아래와 같다. 
- Client와 FastAPI가 통신한 결과(Skeleton point와 metrics)를 전달 받아 핸들링할 수 있으며, Database(MariaDB) 및 NCP Object Storage와 통신 할 수 있다.
- JPA를 이용하여 서버 내에서 데이터들을 영속성 컨텍스트에 관리하고, DataBase에 저장되어 있는 테이블과 엔티티 객체를 매핑하여 사용할 수 있다. 
- QueryDsl를 이용한 동적쿼리 방식으로 Client에게 필요한 데이터를 전달할 수 있다.


