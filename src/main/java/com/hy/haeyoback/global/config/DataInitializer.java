package com.hy.haeyoback.global.config;

import com.hy.haeyoback.domain.learning.entity.LearningContent;
import com.hy.haeyoback.domain.learning.repository.LearningContentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initLearningContents(LearningContentRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new LearningContent(
                        "Spring Boot 기초",
                        "Spring Boot의 기본 개념과 프로젝트 구조를 배워봅니다.",
                        "Backend",
                        "https://example.com/thumbnails/spring-boot-basics.jpg",
                        "# Spring Boot 기초\n\nSpring Boot는 스프링 기반 애플리케이션을 빠르고 쉽게 개발할 수 있도록 도와주는 프레임워크입니다.\n\n## 주요 특징\n- 자동 설정(Auto Configuration)\n- 내장 서버(Embedded Server)\n- 스타터 의존성(Starter Dependencies)\n\n## 시작하기\n1. Spring Initializr에서 프로젝트 생성\n2. IDE에서 프로젝트 열기\n3. @SpringBootApplication 어노테이션 확인\n4. 애플리케이션 실행"
                ));

                repository.save(new LearningContent(
                        "RESTful API 설계",
                        "REST 원칙에 따른 API 설계 방법을 학습합니다.",
                        "Backend",
                        "https://example.com/thumbnails/restful-api.jpg",
                        "# RESTful API 설계\n\nREST(Representational State Transfer)는 웹 서비스 설계 아키텍처 스타일입니다.\n\n## REST 원칙\n1. **Stateless**: 무상태성\n2. **Cacheable**: 캐시 처리 가능\n3. **Uniform Interface**: 일관된 인터페이스\n4. **Client-Server**: 클라이언트-서버 구조\n\n## HTTP 메서드\n- GET: 조회\n- POST: 생성\n- PUT: 수정\n- DELETE: 삭제\n\n## URL 설계 예시\n- GET /api/users - 사용자 목록 조회\n- GET /api/users/{id} - 특정 사용자 조회\n- POST /api/users - 사용자 생성\n- PUT /api/users/{id} - 사용자 수정\n- DELETE /api/users/{id} - 사용자 삭제"
                ));

                repository.save(new LearningContent(
                        "JPA와 Hibernate",
                        "JPA와 Hibernate를 활용한 데이터베이스 연동 방법을 배웁니다.",
                        "Database",
                        "https://example.com/thumbnails/jpa-hibernate.jpg",
                        "# JPA와 Hibernate\n\nJPA(Java Persistence API)는 자바 ORM 기술 표준이며, Hibernate는 JPA의 구현체입니다.\n\n## JPA 핵심 개념\n- **Entity**: 데이터베이스 테이블과 매핑되는 객체\n- **EntityManager**: 엔티티를 관리하는 인터페이스\n- **영속성 컨텍스트**: 엔티티를 영구 저장하는 환경\n\n## 어노테이션\n- @Entity: 엔티티 클래스 지정\n- @Id: 기본 키 지정\n- @GeneratedValue: 기본 키 자동 생성\n- @Column: 컬럼 매핑\n- @Table: 테이블 매핑\n\n## Spring Data JPA\nRepository 인터페이스를 통해 CRUD 작업을 쉽게 구현할 수 있습니다.\n\n```java\npublic interface UserRepository extends JpaRepository<User, Long> {\n    Optional<User> findByEmail(String email);\n}\n```"
                ));

                repository.save(new LearningContent(
                        "Spring Security 기초",
                        "Spring Security를 활용한 인증과 인가를 구현합니다.",
                        "Security",
                        "https://example.com/thumbnails/spring-security.jpg",
                        "# Spring Security 기초\n\nSpring Security는 강력하고 유연한 인증 및 권한 부여 프레임워크입니다.\n\n## 핵심 개념\n1. **Authentication**: 인증 - 사용자가 누구인지 확인\n2. **Authorization**: 인가 - 사용자가 무엇을 할 수 있는지 확인\n\n## 주요 구성요소\n- SecurityFilterChain: 보안 필터 체인\n- UserDetailsService: 사용자 정보 조회\n- PasswordEncoder: 비밀번호 암호화\n- AuthenticationManager: 인증 관리\n\n## JWT 인증\nJSON Web Token을 사용한 Stateless 인증 방식\n\n### JWT 구조\n- Header: 토큰 타입과 알고리즘\n- Payload: 사용자 정보와 권한\n- Signature: 서명\n\n### 장점\n- 서버에 세션 저장 불필요\n- 확장성이 좋음\n- 다양한 플랫폼에서 사용 가능"
                ));

                repository.save(new LearningContent(
                        "Docker 컨테이너 기초",
                        "Docker를 활용한 애플리케이션 컨테이너화를 학습합니다.",
                        "DevOps",
                        "https://example.com/thumbnails/docker-basics.jpg",
                        "# Docker 컨테이너 기초\n\nDocker는 애플리케이션을 컨테이너로 패키징하여 어디서든 실행할 수 있게 해주는 플랫폼입니다.\n\n## Docker의 장점\n- **일관성**: 개발, 테스트, 운영 환경 일치\n- **격리성**: 각 컨테이너는 독립적으로 실행\n- **이식성**: 어떤 환경에서든 동일하게 실행\n- **효율성**: 가상머신보다 가볍고 빠름\n\n## 주요 개념\n1. **Image**: 컨테이너 실행에 필요한 파일과 설정\n2. **Container**: 이미지를 실행한 인스턴스\n3. **Dockerfile**: 이미지를 빌드하는 스크립트\n4. **Docker Compose**: 여러 컨테이너를 정의하고 실행\n\n## 기본 명령어\n```bash\n# 이미지 빌드\ndocker build -t myapp .\n\n# 컨테이너 실행\ndocker run -p 8080:8080 myapp\n\n# 실행 중인 컨테이너 확인\ndocker ps\n\n# 컨테이너 중지\ndocker stop <container-id>\n```\n\n## Docker Compose 예시\n```yaml\nservices:\n  db:\n    image: postgres:16\n    environment:\n      POSTGRES_DB: mydb\n      POSTGRES_USER: user\n      POSTGRES_PASSWORD: password\n  \n  app:\n    build: .\n    ports:\n      - \"8080:8080\"\n    depends_on:\n      - db\n```"
                ));
            }
        };
    }
}
