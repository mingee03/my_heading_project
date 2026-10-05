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


## ④ 개발 과정 요약

1. **도메인 및 DTO 설계:** `Movie`, `MovieRequest`, `MovieResponse` 클래스를 설계했습니다.
2. **Repository 구현:** `MovieRepository` 인터페이스를 만들고, `LinkedHashMap`과 `sequence`를 이용해 CRUD 로직을 구현한 `MovieMemoryRepository`를 작성했습니다.
3. **Service 구현:** `MovieService`에서 비즈니스 로직을 작성하고 데이터가 없을 경우 404를 던지도록 예외 처리를 추가했습니다.
4. **Controller 구현:** `MovieController`에 `@RestController`와 `@RequestMapping`을 붙이고 각 HTTP Method별 API Endpoint를 구현했습니다.
5. **Docker 및 배포 설정:** 애플리케이션을 클라우드에 배포하기 위해 `Dockerfile`을 작성하고 Render와 GitHub를 연동하여 배포했습니다.


## ⑤ 기능 수정·확장 (직접 작성 필요)

**A. 잘못된 입력 처리**
* **기능을 추가한 이유:** 실수나 악의적으로 도메인의 필수 값들을 뺴먹거나, 오입력할 경우를 대비하여 기능을 추가하였습니다. 
* **수정한 클래스와 메서드:** `MovieRequest`의 필드에 `@NotBlank` 추가해서 빈 문자열을 검증하고 `@Min`을 통해 최솟값 검증(0으로 설정하여 음수 방지), `MovieController`에 `@Valid` 추가하여 DTO에 적어둔 규칙 검증 실행하게 했습니다.
* **실제 응답 결과:** 

**B. 조회 기능 확장**
* **기능을 추가한 이유:** 특정 장르를 검색하여 원하는 장르의 영화를 찾을 수 있게 했습니다.
* **수정한 클래스와 메서드:** MovieController.java: findAll() 메서드 파라미터에 @RequestParam(required = false) String genre 추가, MovieService.java: findAll(String genre) 메서드 내부에 filter()를 사용하여 장르가 일치하는 데이터만 걸러내는 기능 추가
* **실제 응답 결과:** 


## ⑥ 배포 과정 요약

1. **빌드 및 배포 순서:** 코드를 GitHub 개인 리포지토리에 커밋 및 푸시한 뒤, Render.com에서 New Web Service를 생성하여 해당 리포지토리를 연동했습니다.
2. **배포를 위해 추가/수정한 파일:** `Dockerfile`을 프로젝트 최상단에 추가했습니다.
3. **배포 중 발생한 문제와 해결 방법:** 
   * **문제:** Render에서 빌드 중 `Cannot find a Java installation... matching: {languageVersion=21}` 에러가 발생했습니다.
   * **해결:** `build.gradle`에는 Java 21로 설정되어 있었으나 초기 `Dockerfile`이 17 버전이었기 때문에 충돌이 발생했습니다. `Dockerfile`의 이미지를 `eclipse-temurin:21-jdk-alpine`으로 수정하여 해결했습니다.
4. **배포 URL로 확인한 요청과 응답:** 
   * 위에 나온 실행 결과 캡쳐화면에서 잘 작동하는 것을 확인할 수 있습니다.


## ⑦ Weekly Report (일부 직접 작성 필요)

**Key Learning (직접 구현하며 이해한 내용 3가지)**
1. HTTP 요청을 받고 응답을 반환하는 역할은 Controller가, 핵심 비즈니스 로직과 예외 처리는 Service가, 실제 데이터 저장 및 관리는 Repository가 전담하도록 분리함으로써 코드가 훨씬 깔끔해지고 유지보수하기 쉬워진다는 것을 배웠습니다
2. DTO를 사용하여 화면에 필요한 데이터만 골라서 주고받거나 @NotBlank 같은 입력 규칙을 도메인과 분리해 안전하게 관리할 수 있다는 점을 배웠습니다.
3. Render나 Postman을 통해 만든 서비스를 편하게 테스트할 수 있다는 것을 체감했습니다. 

**Problem & Solution (개발 중 겪은 문제와 해결 과정)**
* **문제:** Postman으로 `POST` 요청을 보냈을 때 저장된 결과의 모든 값이 `null`로 나오는 문제가 있었습니다.
* **해결 과정:** 전달된 데이터를 받는 DTO 클래스는 문제가 없었으나, 도메인 클래스인 `Movie.java`의 생성자 내부를 잊고 채워 넣지 못했던 것을 발견하여 해결하였습니다.  

**Code Review (본인이 작성한 중요한 메서드 1개와 동작 설명)**
* **메서드:** 
* public List<MovieResponse> findAll(String genre) {
  List<Movie> movies = repository.findAll();
  if (genre != null && !genre.isEmpty()) {
  movies = movies.stream().filter(m -> m.getGenre().equalsIgnoreCase(genre)).collect(Collectors.toList());
  }
  return movies.stream().map(this::toResponse).toList();
  }
* **동작 설명:** 
* 먼저 List<Movie>를 꺼내옵니다. 만약 파라미터로 전달된 genre 값이 존재한다면, Java Stream API의 .filter() 기능을 사용하여 대소문자 구분 없이 해당 장르와 일치하는 영화만 남기도록 리스트를 걸러냅니다. 마지막으로 .map(this::toResponse)를 사용해 걸러진 도메인 객체(Movie)들을 MovieResponse DTO로 변환하여 리스트 형태로 반환합니다. 

**AI Usage (AI 활용 내용)**
* **질문한 내용:** CRUD 구현 순서, Render 배포 권한 오류(조직 리포지토리 접근 불가), Postman 테스트 시 null 반환 오류
* **참고한 답변 및 수정:** 개발 가이드 및 구현 순서에 대한 조언을 참고하여 전체 구조를 잡았고, 생성자 할당 누락 문제를 지적받아 해당 부분을 수정했습니다.

**Reflection (더 공부하고 싶은 내용 또는 궁금한 점)**
* solution 코드를 많이 참고하면서 코드를 만들긴 했지만, 처음부터 하나하나 만들다 보니 어떤 메서드가 어떤 기능을 하면서 상호작용하는 지 더 잘 이해하게 된 것 같습니다. 기능 확장 추가 부분에서는 어떻게 추가 할 지 몰라 AI에게 조언을 구했는데, 이 부분을 더 알아내어 스스로도 
더 많은 기능을 구현할 수 있게 되고 싶었습니다. 
