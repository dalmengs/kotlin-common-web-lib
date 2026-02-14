# Kotlin Common Web Library

Spring Boot Kotlin 애플리케이션을 위한 공통 웹 유틸리티 라이브러리입니다.

## 기능

- **BaseResponse**: 표준화된 API 응답 형식
- **BaseException**: 커스텀 예외 처리 기반 클래스
- **GlobalExceptionHandler**: 전역 예외 처리 핸들러

## 설치

### Gradle (build.gradle.kts)

```kotlin
repositories {
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/dalmengs/kotlin-common-web-lib")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_USERNAME")
            password = project.findProperty("gpr.token") as String? ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    implementation("com.dalmeng:kotlin-common-web-lib:1.0.0")
}
```

### Maven (pom.xml)

```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/dalmengs/kotlin-common-web-lib</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.dalmeng</groupId>
        <artifactId>kotlin-common-web-lib</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

## 사용 방법

### 1. BaseResponse 사용

컨트롤러에서 `BaseResponse`를 사용하여 표준화된 응답을 반환합니다:

```kotlin
import com.dalmeng.common.web.response.BaseResponse

@RestController
@RequestMapping("/api/problems")
class ProblemController {
    
    @PostMapping
    fun create(
        @RequestHeader("X-User-Id") userId: String,
        @RequestBody @Valid request: CreateProblemRequest,
    ): BaseResponse<ProblemResponse> =
        BaseResponse.ok(data = problemService.create(userId, request))
    
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): BaseResponse<ProblemResponse> =
        BaseResponse.ok(data = problemService.getById(id))
}
```

### 2. BaseException 사용

커스텀 예외를 만들 때 `BaseException`을 상속받습니다:

```kotlin
import com.dalmeng.common.web.exception.BaseException

class NotFoundException(message: String) : BaseException(404, message)
class BadRequestException(message: String) : BaseException(400, message)
class UnauthorizedException(message: String) : BaseException(401, message)
class ForbiddenException(message: String) : BaseException(403, message)
```

### 3. GlobalExceptionHandler

`GlobalExceptionHandler`는 자동으로 등록되어 모든 예외를 처리합니다:

- `BaseException`을 상속한 예외는 해당 `statusCode`로 응답
- 기타 예외는 500 Internal Server Error로 응답

```kotlin
// 서비스에서 예외 발생
throw NotFoundException("Problem not found with id: $id")

// 자동으로 다음 형식으로 응답됨:
// {
//   "statusCode": 404,
//   "message": "Problem not found with id: 123",
//   "error": {
//     "message": "Problem not found with id: 123"
//   }
// }
```

## 응답 형식

### 성공 응답

```json
{
  "statusCode": 200,
  "message": "Succeed",
  "data": {
    // 응답 데이터
  }
}
```

### 에러 응답

```json
{
  "statusCode": 404,
  "message": "Problem not found",
  "error": {
    "message": "Problem not found"
  }
}
```

## 요구사항

- Java 17 이상
- Kotlin 1.9.22 이상
- Spring Boot 3.2.0 이상

## 라이선스

Apache License 2.0

## 저장소

https://github.com/dalmengs/kotlin-common-web-lib
