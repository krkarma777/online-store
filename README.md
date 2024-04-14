# SEED: Open Market Project

## 설명

SEED는 JDK 17, Oracle DB 19c, Spring Boot, JPA, Thymeleaf를 기반으로 구현된 오픈마켓 웹 프로젝트입니다. 본 프로젝트는 테스트 주도 개발(TDD)과 RESTful 아키텍처를 중심으로 개발되어, 복잡한 시스템의 효과적인 관리와 고품질 코드 작성에 초점을 맞추고 있습니다. 사용자와 판매자 모두를 위한 다양한 기능을 포함하며, Spring Security 6, JWT, OAuth2 등 최신 웹 기술을 통해 사용자 경험과 보안성을 강화한 프로젝트입니다.

## 특징

- **RESTful Architecture**: 전통적인 MVC 패턴에서 API 중심 아키텍처로 전환하여, 코드 중복 감소와 유지보수 향상을 도모했습니다.
- **Security Implementations**: Spring Security와 JWT를 사용한 강력한 인증 및 권한 부여 프로세스 구현.
- **Dynamic Content Management**: Thymeleaf와 함께 다이나믹 웹 콘텐츠를 효율적으로 관리합니다.
- **E-Commerce Ready**: 다양한 결제 API 통합으로 사용자와 판매자에게 매끄러운 거래 경험 제공.

## 기술 스택

- JDK 17
- Oracle DB 19c
- Spring Boot
- JPA & Hibernate
- Thymeleaf
- Spring Security, OAuth2, JWT
- Daum Address API, CKEditor5, Chart JS
- Kakao and Naver Login API
- PayPal API

### 시작하기

- JDK 17
- Oracle DB 19c 설치 필요
- 필요한 모든 의존성은 `build.gradle`에 명시되어 있습니다.
  
1. 프로젝트 클론: git clone https://github.com/krkarma777/online-store

# 환경 설정

이 프로젝트를 올바르게 실행하기 위해서는 여러 환경 설정이 필요합니다. 아래에 필요한 구성 상세 정보를 제공합니다.

## 데이터베이스 구성

Oracle DB가 설치되어 있고 실행 중인지 확인하세요. 데이터베이스 연결을 위한 다음 환경 변수를 설정하세요.

- `SPRING_DATASOURCE_URL`: Oracle 데이터베이스 연결을 위한 JDBC URL.
- `SPRING_DATASOURCE_USERNAME`: 데이터베이스 사용자 이름.
- `SPRING_DATASOURCE_PASSWORD`: 데이터베이스 비밀번호.

로컬 설정 예시:
```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1522:orcl
spring.datasource.username=사용자이름
spring.datasource.password=비밀번호
```

## 이메일 서비스 구성
알림 및 확인 이메일을 보낼 수 있도록 메일 서버를 구성합니다.

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=yourEmail@gmail.com
spring.mail.password=앱비밀번호
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

## OAuth2 구성
각 서비스(카카오, 네이버 등)에 대한 클라이언트 ID, 클라이언트 비밀번호 및 리다이렉트 URI를 지정하여 OAuth2 제공자를 설정하세요. 이 값들은 버전 관리 시스템에 노출되지 않도록 안전하게 저장되어야 합니다.

```
spring.security.oauth2.client.registration.kakao.client-id=카카오클라이언트ID
spring.security.oauth2.client.registration.kakao.client-secret=카카오클라이언트비밀번호
spring.security.oauth2.client.registration.naver.client-id=네이버클라이언트ID
spring.security.oauth2.client.registration.naver.client-secret=네이버클라이언트비밀번호
```

## JWT 비밀 키
JWT 토큰을 서명하는 데 사용되는 비밀 키를 구성하세요. 이 키는 기밀로 유지되어야 합니다.

```
spring.jwt.secret=비밀키
```

## 애플리케이션 실행
모든 구성이 설정되면, 다음을 사용하여 애플리케이션을 실행할 수 있습니다.

```
./gradlew bootRun
```

## 추가 사항
- 요구 사항에 맞게 로깅 설정을 조정하여 디버깅 및 모니터링을 개선하세요.
- 처리할 예정인 파일 크기를 기반으로 멀티파트 설정을 수정하세요.
- 애플리케이션의 보안 설정을 주기적으로 검토하고 업데이트하여 강력한 보안을 유지하세요.

### Contributing

모든 종류의 기여를 환영합니다. 이슈 트래킹, 풀 리퀘스트 등을 통해 참여해주세요. 기여 전에 `CONTRIBUTING.md`를 확인해주세요.

### License

이 프로젝트는 MIT 라이센스를 따릅니다. 자세한 내용은 `LICENSE` 파일을 참조하십시오.

### Contact

오유준 - krkarma777@gmail.com

Project Link: [https://github.com/krkarma777/online-store](https://github.com/krkarma777/online-store)













- **Features and Screenshots**: [Link](https://krkarma777.notion.site/SEED-a2c911191c124a29b57b3f1f841c7264)

