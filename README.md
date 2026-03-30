# 🧩 Mujakjung-be (무작정 백엔드)

> **Spring Boot 기반의 안정적인 회원 관리 시스템**
> JPA와 MySQL을 활용하여 효율적인 데이터 처리와 확장성 있는 API 구조를 지향합니다.

---

## 🚀 Tech Stack

| 분류 | 기술 스택 |
| :--- | :--- |
| **Language** | Java 17+ |
| **Framework** | Spring Boot 3.5.2 |
| **Data JPA** | Hibernate 6.6.18 |
| **Database** | MySQL 8.4.5 (Docker) |
| **Security** | Spring Security / OAuth 2.0 (Kakao) |
| **Build Tool** | Gradle |

---

## 📦 주요 기능

- **회원 관리 (Member)**
  - [x] JPA 연동 회원 CRUD 기본 구현
  - [x] Spring Security 기반 보안 설정
- **소셜 로그인 (OAuth 2.0)**
  - [x] 카카오 로그인 연동 (인가 코드 및 토큰 발급)
  - [x] 카카오 유저 프로필 정보 연동
- **인증 및 권한 (진행 중)**
  - [ ] JWT(JSON Web Token) 필터 및 토큰 관리
  - [ ] 관리자 전용 회원 조회/관리 (예정)

---

## ⚙️ 실행 방법

### 1️⃣ MySQL 실행 (Docker)
로컬 환경에서 MySQL 컨테이너를 실행합니다. (포트: 3361)

```bash
docker run -d \
  --name mysql \
  -p 3361:3306 \
  -e MYSQL_ROOT_PASSWORD=비밀번호 \
  -e MYSQL_DATABASE=mujakjung \
  -e MYSQL_USER=mujakjung \
  -e MYSQL_PASSWORD=1234 \
  mysql:8
2️⃣ application.properties 설정
src/main/resources/application.properties 파일에 아래 설정을 적용하세요.

Properties
spring.datasource.url=jdbc:mysql://localhost:3361/mujakjung
spring.datasource.username=mujakjung
spring.datasource.password=1234
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
3️⃣ 서버 실행
Bash
./gradlew bootRun
또는 IDE(IntelliJ)에서 MujakjungBeApplication을 실행하세요.

🗂️ 프로젝트 구조
Plaintext
com.it.Mujakjung_be
 ├─ global           # 전역 설정 (Kakao, Security, JWT 등)
 ├─ member           # 회원 도메인
 │   ├─ controller   # API 엔드포인트
 │   ├─ service      # 비즈니스 로직
 │   ├─ repository   # 데이터 접근 (JPA)
 │   └─ entity       # DB 테이블 매핑
 └─ MujakjungBeApplication
📌 향후 계획
[ ] JWT 로그인: Access/Refresh Token 발급 및 검증 로직 완성

[ ] 예외 처리: @RestControllerAdvice를 활용한 공통 에러 응답 처리

[ ] API 문서화: Swagger(SpringDoc) 도입

[ ] 테스트: JUnit5 기반 서비스 레이어 테스트 코드 작성
