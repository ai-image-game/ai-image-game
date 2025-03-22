# AI Image Game

AI를 활용한 이미지 게임 프로젝트입니다.
Front Web과 Websocket으로 통신하는 Backend 서버 소스입니다.

## 프로젝트 구조

```
src/main/java/ai/imagegame/
├── config/         # 설정 관련 클래스
├── controller/     # API 엔드포인트 컨트롤러
├── domain/         # 도메인 모델 (DB)
├── dto/            # 데이터 전송 객체 (Front)
├── exception/      # 예외 처리
├── repository/     # 데이터 접근 Repository
├── service/        # 비즈니스 로직
└── util/           # 유틸리티 클래스
```

## 기술 스택

- Java 22
- Spring Boot 3.3.1
- Gradle 8.9
- MySQL 
- Redis
- JPA
- Websocket

## 환경 설정

1. `.env` 파일에 필요한 환경 변수를 설정합니다.
2. `build.gradle` 파일에서 프로젝트 의존성을 확인할 수 있습니다.

## 실행 방법

1. 프로젝트 클론
```bash
git clone [repository-url]
```

2. Gradle 빌드
```bash
./gradlew build
```

3. 애플리케이션 실행
```bash
./gradlew bootRun
```