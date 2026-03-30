 Mujakjung-be (무작정 백엔드)Spring Boot 기반의 안정적인 회원 관리 시스템 > JPA와 MySQL을 활용하여 효율적인 데이터 처리와 확장성 있는 API 구조를 지향합니다.🚀 Tech StackCategoryTechnologyLanguageJava 17+FrameworkSpring Boot 3.xData JPAHibernateDatabaseMySQL 8.0 (Docker)View EngineThymeleafBuild ToolGradle📦 주요 기능회원 관리 (Member)[x] Spring Security 기반 회원 가입 및 로그인[x] 회원 정보 조회, 수정, 삭제 (CRUD)[ ] 관리자 전용 전체 회원 조회 (예정)인증 및 보안[x] 카카오 소셜 로그인 연동 (OAuth 2.0)[ ] JWT(JSON Web Token) 도입 (진행 중)⚙️ 실행 방법1️⃣ Database 실행 (Docker)로컬 환경에서 MySQL 8.0 버전을 컨테이너로 실행합니다. (포트: 3361)Bashdocker run -d \
  --name mysql \
  -p 3361:3306 \
  -e MYSQL_ROOT_PASSWORD=비밀번호 \
  -e MYSQL_DATABASE=mujakjung \
  -e MYSQL_USER=mujakjung \
  -e MYSQL_PASSWORD=1234 \
  mysql:8
2️⃣ application.properties 설정src/main/resources/application.properties 파일에 아래 설정을 확인해주세요.Propertiesspring.datasource.url=jdbc:mysql://localhost:3361/mujakjung
spring.datasource.username=mujakjung
spring.datasource.password=1234
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
3️⃣ 서버 실행Bash./gradlew bootRun
또는 IDE(IntelliJ)에서 MujakjungBeApplication을 실행하세요.🗂️ 프로젝트 구조Plaintextcom.it.Mujakjung_be
 ├─ global      # 공통 설정 (Security, OAuth, JWT 등)
 ├─ member      # 회원 도메인
 │   ├─ controller
 │   ├─ service
 │   ├─ repository
 │   └─ entity
 └─ MujakjungBeApplication
📌 향후 계획[ ] JWT 로그인: Access/Refresh Token을 활용한 보안 강화[ ] 예외 처리: @RestControllerAdvice를 통한 전역 예외 처리 공통화[ ] API 응답 표준화: 공통 Response DTO 적용[ ] 테스트: JUnit5를 활용한 단위 및 통합 테스트 작성[ ] 배포: AWS 또는 Docker를 활용한 배포 환경 구성👨‍💻 AuthorSongGunHo (@ghson)Junior AI & Backend Developer
