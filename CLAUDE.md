# Steam Clone Project

게임 플랫폼 Steam의 기능을 구현하는 백엔드 프로젝트

## 기술 스택

- **Language**: Java 17
- **Framework**: Spring Boot 3.4.3
- **Security**: Spring Security + JWT
- **Database**: MySQL (운영), H2 (테스트), Redis (캐시/세션)
- **ORM**: Spring Data JPA
- **Template**: Thymeleaf
- **Build**: Gradle
- **기타**: WebSocket, EhCache, Actuator, Prometheus

## 프로젝트 구조

```
src/main/java/com/example/steam/
├── core/                    # 공통 인프라 및 설정
│   ├── config/              # 설정 클래스 (Redis, 부하테스트 등)
│   ├── filter/              # 필터
│   ├── init/                # 초기화
│   ├── security/            # 보안 설정, JWT
│   └── utils/               # 유틸리티
│
└── module/                  # 비즈니스 모듈
    ├── article/             # 게시글
    ├── chat/                # 채팅
    ├── comment/             # 댓글
    ├── company/             # 회사(퍼블리셔)
    ├── discount/            # 할인
    ├── email/               # 이메일
    ├── friendship/          # 친구
    ├── gallery/             # 갤러리
    ├── home/                # 홈
    ├── login/               # 로그인
    ├── member/              # 회원
    ├── order/               # 주문
    ├── product/             # 상품(게임)
    └── shoppingCart/        # 장바구니
```

각 모듈은 다음 레이어 구조를 따름:
- `domain/` - 엔티티, 도메인 모델
- `application/` - 서비스 로직
- `presentation/` - 컨트롤러
- `repository/` - 데이터 접근
- `dto/` - 데이터 전송 객체

## 빌드 & 실행

```bash
# 빌드
./gradlew build

# 테스트 (H2 + test 프로필)
./gradlew test

# 로컬 실행
./gradlew bootRun

# Docker 실행
docker-compose -f docker-compose.local.yaml up
```

## 프로필

- `local` - 로컬 개발 환경
- `test` - 테스트 환경 (H2)
- `prod` - 운영 환경

## 코딩 규칙

- Lombok 사용 (`@Getter`, `@Builder`, `@RequiredArgsConstructor` 등)
- 새 모듈 추가 시 기존 모듈 구조(domain/application/presentation/repository/dto) 준수
- 테스트 작성 시 `@ActiveProfiles("test")` 사용
- 환경변수는 `.env` 파일 사용 (커밋 금지, `.env.example` 참고)
