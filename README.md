# 🎬 영화 관리 REST API 프로젝트 (Movie CRUD)

## ① 프로젝트 소개

**1. 주제와 관리하는 데이터**
본 프로젝트는 **영화(Movie) 정보 관리 시스템**입니다. 영화의 ID, 제목, 감독, 장르, 개봉 연도, 관객수 등의 데이터를 관리합니다. Database 연동 없이 Java Collection(`LinkedHashMap`)을 활용한 In-Memory 저장소로 동작합니다.

**2. 프로젝트 구조**
```
src/main/java/org/example/my_heading_project_01/
 ├── controller/ MovieController.java      # 클라이언트의 HTTP 요청을 받고 응답을 반환
 ├── service/    MovieService.java         # 핵심 비즈니스 로직 및 예외 처리
 ├── repository/ MovieRepository.java      # 저장소 인터페이스
 ├── repository/ MovieMemoryRepository.java# In-Memory 기반 저장소 구현체
 ├── domain/     Movie.java                # 핵심 데이터 모델 (Domain)
 └── dto/        MovieRequest.java, MovieResponse.java # 데이터 교환 객체
```

**3. 로컬 실행 방법**
1. 리포지토리를 클론합니다.
2. 터미널(또는 IntelliJ)에서 `./gradlew bootRun` 명령어를 실행합니다.
3. `http://localhost:8080` 에서 서버가 구동됩니다.

**4. API Endpoint 표**

| 기능 | HTTP Method | URL |
|---|---|---|
| 등록 | POST | `/api/movies` |
| 전체 조회 | GET | `/api/movies` |
| 단건 조회 | GET | `/api/movies/{id}` |
| 수정 | PUT | `/api/movies/{id}` |
| 삭제 | DELETE | `/api/movies/{id}` |

**5. 요청·응답 JSON 예시**
* **요청 (POST / PUT):**
```json
{
  "title": "인셉션",
  "director": "크리스토퍼 놀란",
  "genre": "SF",
  "year": "2010",
  "view": 1500000
}
```
* **응답 (GET / POST 등):**
```json
{
  "id": 1,
  "title": "인셉션",
  "director": "크리스토퍼 놀란",
  "genre": "SF",
  "year": "2010",
  "view": 1500000
}
```

**6. Repository 및 배포 URL**
* **GitHub Organization Repository URL:** https://github.com/2026-2-WebService/assign05-c01-22200439.git]
* **Personal GitHub Repository URL:** https://github.com/mingee03/my_heading_project.git
* **배포 URL (Render):** https://my-heading-project.onrender.com/api/movies

---

## ② 개발환경 및 Dependency

| 항목 | 작성 내용 |
|---|---|
| **IDE** | IntelliJ IDEA |
| **JDK** | JDK 21 (Temurin 21) |
| **Spring Boot** | 3.3.x |
| **Build Tool** | Gradle 8.x |
| **데이터 저장** | `LinkedHashMap`을 활용한 인메모리 저장소 |
| **배포 환경** | Render.com Web Service (Docker 배포) |

**사용한 Dependency 및 이유**
* `spring-boot-starter-web`: RESTful 웹 서비스(API)를 구축하기 위해 필수적인 Spring MVC, 내장 Tomcat 서버 및 JSON 직렬화(Jackson) 등을 제공하므로 사용했습니다.

---

## ③ Solution 분석 (STUDY_GUIDE.md 분석)

**Q1. Controller → Service → Repository의 요청 처리 흐름은 어떻게 되나요?**
A: 클라이언트가 URL로 HTTP 요청을 보내면 `BookController`의 메서드(`@GetMapping`, `@PostMapping` 등)가 이를 받습니다. 컨트롤러는 데이터 처리를 위해 `BookService`의 메서드를 호출하고, 서비스는 실제 데이터 저장/조회를 위해 `BookRepository`의 메서드를 호출합니다. 최종적으로 `BookMemoryRepository`가 메모리(Map)에서 데이터를 꺼내 역순으로 반환합니다.

**Q2. BookRequest, Book, BookResponse의 역할은 각각 무엇인가요?**
A: `BookRequest`(DTO)는 클라이언트가 서버로 데이터를 등록하거나 수정할 때 보내는 값을 담는 객체입니다. `Book`(Domain)은 애플리케이션 핵심 데이터 구조를 정의하며 Repository에 실제 저장되는 형태입니다. `BookResponse`(DTO)는 서버가 클라이언트에게 안전하게 데이터를 가공하여 내려줄 때 사용하는 응답 전용 객체입니다.

**Q3. 새 데이터의 ID가 생성되는 위치는 어디인가요?**
A: `BookMemoryRepository` 클래스의 `save()` 메서드 내부에서 생성됩니다. 저장소 내에 선언된 `sequence` 변수를 1씩 증가(`++sequence`)시켜 새로운 도메인 객체의 `id` 필드에 할당합니다.

**Q4. 존재하지 않는 ID에 대해 404가 반환되는 과정은 어떻게 되나요?**
A: `BookService` 클래스의 `findById()` 등의 메서드 내부에서 데이터를 찾을 때, 반환된 Optional 객체가 비어있을 경우 `orElseThrow()`를 사용하여 `ResponseStatusException(HttpStatus.NOT_FOUND)` 예외를 발생시킵니다. 이 예외가 발생하면 Spring 프레임워크가 이를 감지하여 404 Not Found HTTP 응답으로 변환합니다.

**Q5. Domain 객체를 Response DTO로 변환하는 과정은 어디서 이루어지나요?**
A: 주로 Service 계층에서 이루어집니다. `BookService` 클래스 내부에 `toResponse(Book book)`와 같은 변환용 프라이빗 메서드를 만들어두고, Repository에서 반환된 도메인 객체(`Book`)를 이 메서드를 통해 `BookResponse`로 래핑하여 Controller로 넘깁니다.

---

## ④ 개발 과정 요약

1. **도메인 및 DTO 설계:** 요구사항(5개 이상의 필드)에 맞춰 `Movie`, `MovieRequest`, `MovieResponse` 클래스를 설계했습니다.
2. **저장소(Repository) 구현:** `MovieRepository` 인터페이스를 만들고, `LinkedHashMap`과 `sequence`를 이용해 CRUD 로직을 구현한 `MovieMemoryRepository`를 작성했습니다.
3. **서비스(Service) 구현:** `MovieService`에서 비즈니스 로직을 작성하고 데이터가 없을 경우 404를 던지도록 예외 처리를 추가했습니다.
4. **컨트롤러(Controller) 구현:** `MovieController`에 `@RestController`와 `@RequestMapping`을 붙이고 각 HTTP Method별 API Endpoint를 구현했습니다.
5. **Docker 및 배포 설정:** 애플리케이션을 클라우드에 배포하기 위해 JDK 21 환경에 맞춘 `Dockerfile`을 작성하고 Render와 GitHub를 연동하여 배포했습니다.

---

## ⑤ 기능 수정·확장 (직접 작성 필요)

> 💡 **[안내]** 이 부분은 과제의 **[STEP 5. 기능 수정·확장]**에 해당하는 내용입니다.
> 현재 코드에는 A(입력 검증)와 B(조회 확장)가 아직 구현되지 않았습니다. 코드를 추가로 작성하신 뒤 아래 내용을 채워주세요!

**A. 잘못된 입력 처리 (예: 제목 빈칸 금지, 관객수 음수 금지 등)**
* **기능을 추가한 이유:** [여기에 작성하세요]
* **수정한 클래스와 메서드:** [여기에 작성하세요. 예: `MovieRequest`의 필드에 `@NotBlank` 추가, `MovieController`에 `@Valid` 추가 등]
* **테스트 요청/예상 결과:** [여기에 작성하세요]
* **실제 응답 결과:** [여기에 작성하세요]

**B. 조회 기능 확장 (예: 장르별 필터링, 제목 검색 등)**
* **기능을 추가한 이유:** [여기에 작성하세요]
* **수정한 클래스와 메서드:** [여기에 작성하세요]
* **테스트 요청/예상 결과:** [여기에 작성하세요]
* **실제 응답 결과:** [여기에 작성하세요]

---

## ⑥ 배포 과정 요약

1. **빌드 및 배포 순서:** 코드를 GitHub 개인 리포지토리에 커밋 및 푸시한 뒤, Render.com에서 New Web Service를 생성하여 해당 리포지토리를 연동했습니다.
2. **배포를 위해 추가/수정한 파일:** JDK 21 버전을 사용하는 `Dockerfile`을 프로젝트 최상단에 추가했습니다.
3. **배포 중 발생한 문제와 해결 방법:** 
   * **문제:** Render에서 빌드 중 `Cannot find a Java installation... matching: {languageVersion=21}` 에러가 발생했습니다.
   * **해결:** `build.gradle`에는 Java 21로 설정되어 있었으나 초기 `Dockerfile`이 17 버전이었기 때문에 충돌이 발생했습니다. `Dockerfile`의 이미지를 `eclipse-temurin:21-jdk-alpine`으로 수정하여 해결했습니다.
4. **배포 URL로 확인한 요청과 응답:** 
   * [배포된 주소에 Postman으로 요청한 결과(201 Created, 200 OK 등)가 잘 나왔는지 간단히 적어주세요]

---

## ⑦ Weekly Report (일부 직접 작성 필요)

**Key Learning (직접 구현하며 이해한 내용 3가지)**
1. [여기에 작성하세요. 예: Controller, Service, Repository의 명확한 역할 분담 체계]
2. [여기에 작성하세요. 예: Spring Boot에서 HTTP 메서드를 매핑하고 JSON을 주고받는 원리]
3. [여기에 작성하세요. 예: DTO와 도메인 객체를 분리하는 이유와 장점]

**Problem & Solution (개발 중 겪은 문제와 해결 과정)**
* **문제:** Postman으로 `POST` 요청을 보냈을 때 저장된 결과의 모든 값이 `null`로 나오는 문제가 있었습니다.
* **해결 과정:** 전달된 데이터를 받는 DTO 클래스는 문제가 없었으나, 도메인 클래스인 `Movie.java`의 생성자 내부에서 매개변수를 클래스 필드에 할당하는 로직(`this.title = title;` 등)이 누락되어 있었습니다. 생성자 내부 로직을 수정하여 정상적으로 값이 들어가도록 고쳤습니다.

**Code Review (본인이 작성한 중요한 메서드 1개와 동작 설명)**
* **메서드:** [여기에 본인이 가장 고민했거나 중요하다고 생각하는 메서드를 복사해 넣으세요]
* **동작 설명:** [해당 코드가 어떻게 동작하는지 자신의 언어로 설명하세요]

**AI Usage (AI 활용 내용)**
* **질문한 내용:** CRUD 구현 순서, Render 배포 권한 오류(조직 리포지토리 접근 불가), Postman 테스트 시 null 반환 오류
* **참고한 답변 및 수정:** 개발 가이드 및 구현 순서에 대한 조언을 참고하여 전체 구조를 잡았고, 조직 리포지토리 설정 제한으로 인해 개인 리포지토리로 분리하는 방법을 안내받아 해결했습니다. 또한 생성자 할당 누락 문제를 지적받아 해당 부분을 수정했습니다.

**Reflection (더 공부하고 싶은 내용 또는 궁금한 점)**
* [여기에 과제를 마치며 느낀 점이나 더 알아보고 싶은 기술(DB 연동 등)을 자유롭게 적어주세요]
