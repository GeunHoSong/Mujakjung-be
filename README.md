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
### application.properties 설정
  spring.datasource.url=jdbc:mysql://localhost:3361/mujakjung
  spring.datasource.username=mujakjung
  spring.datasource.password=1234
  spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
