🧩 Mujakjung-be

Spring Boot 기반 백엔드 프로젝트입니다.
회원 관리 기능을 중심으로 JPA와 MySQL을 연동하여 개발하고 있습니다.

🚀 Tech Stack

Java 17+

Spring Boot

Spring Data JPA (Hibernate)

MySQL (Docker)

Thymeleaf

Gradle / Maven (프로젝트에 맞게)

📦 주요 기능

회원 가입

로그인

회원 정보 조회/수정/삭제

관리자 회원 조회 (예정)

⚙️ 실행 방법
1️⃣ MySQL 실행 (Docker)
docker run -d \
  --name mysql \
  -p 3361:3306 \
  -e MYSQL_ROOT_PASSWORD=비밀번호 \
  -e MYSQL_DATABASE=mujakjung \
  -e MYSQL_USER=mujakjung \
  -e MYSQL_PASSWORD=1234 \
  mysql:8
2️⃣ application.properties 설정
spring.datasource.url=jdbc:mysql://localhost:3361/mujakjung
spring.datasource.username=mujakjung
spring.datasource.password=1234
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
3️⃣ 서버 실행
./gradlew bootRun

또는 IDE에서 MujakjungBeApplication 실행

🗂️ 프로젝트 구조
com.it.Mujakjung_be
 ├─ global
 ├─ member
 │   ├─ controller
 │   ├─ service
 │   ├─ repository
 │   └─ entity
 └─ MujakjungBeApplication
🔥 개발 환경

Server Port: 8080

Database: MySQL (localhost:3361)

JPA ddl-auto: update (개발용)

📌 향후 계획

JWT 로그인

예외 처리 공통화

API 응답 표준화

테스트 코드 작성

배포 환경 구성

👨‍💻 Author

SongGunHo
