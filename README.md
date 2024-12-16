Ball Framework
---

## 🔗 Table of Contents

<p>I. [📍 Overview](#-overview)</p>
<p>II. [📁 Project Structure](#-project-structure)</p>

---

## 📍 Overview

'Ball Framework' 는 'Spring Framework' 를 이용하여 엔터프라이즈용 코틀린 백엔드 어플리케이션을 구축하는 프레임워크입니다.
'Spring Web Mvc' 를 이용하여 빠르게 엔터프라이즈용 어플리케이션을 구축하는 것과, 기반 요소(infrastructure)의 변경에 영향을 받지 않는 핵심 코드를 작성하는 것을 목표로 합니다.

'Ball Framework' 는 'Data Revision 관리', '사용자 인증/인가', '분산 락 처리', 로깅 등의 기능을 제공합니다.

---

## 📁 Project Structure

```text
└── ball-framework
    ├── application
    │   └── src
    ├── core-utils
    │   └── src
    ├── domain
    │   └── src
    ├── infra
    │   ├── infra-auth-jpa
    │   ├── infra-auth-redis
    │   ├── infra-jpa
    │   ├── infra-mongodb
    │   ├── infra-redis
    │   └── infra-spring-security
    ├── presentation
    │   └── presentation-mvc
    └── sample-project
        └── src
```

### core-utils

```text
├── extension
│   └── ArrowExtension.kt
├── type
│   ├── BallRequestContext.kt
│   ├── BinaryId.kt
│   ├── LogicError.kt
│   └── ValidType.kt
└── utils
    ├── Clock.kt
    ├── JsonUtils.kt
    ├── Korean.kt
    ├── Logger.kt
    ├── StringUtils.kt
    └── ToStringBuilder.kt
```

함수형 로직 작성을 위한 '[Arrow](https://arrow-kt.io)' 의 확장과 로깅, Json 직렬화 등 여러가지 유틸을 제공합니다.

#### BinaryId.kt

```kotlin
@JvmInline
value class BinaryId(val value: ByteArray) : Comparable<BinaryId> {
    companion object {
        fun new(): BinaryId = BinaryId(ULID.nextULID().toBytes())

        fun fromString(value: String): BinaryId = BinaryId(ULID.parseULID(value).toBytes())
    }

    init {
        runCatching { ULID.fromBytes(value).toBytes() }.getOrNull()
            ?: throw LogicError("Invalid ULID payload")
    }

    override fun compareTo(other: BinaryId): Int = compareValuesBy(this, other)

    override fun toString(): String {
        return ULID.fromBytes(value).toString()
    }

    fun toHexString(): String = "0x${this.value.joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }}"
}
```

'Ball Framework' 는 `AUTO_INCREMENT`를 지양합니다. 분산형 시스템과 다양한 DBMS 를 사용하는 것을 고려하여, PK
값으로 '[ULID](https://github.com/ulid/spec)' 를 사용하는 것과 이를 `BINARY(16)` 혹은 `STRING` 타입으로 저장하는 것을 권장합니다.

### domain

```text
├── exception
│   └── EntityNotFoundError.kt
├── model
│   ├── EntityBase.kt
│   ├── RepositoryBase.kt
│   ├── SearchRepository.kt
│   ├── auth
│   │   ├── RefreshToken.kt
│   │   ├── RefreshTokenRepository.kt
│   │   ├── Token.kt
│   │   ├── UserPrincipal.kt
│   │   └── UserPrincipalContextHolder.kt
│   ├── revision
│   │   ├── BallRevisionDto.kt
│   │   └── BallRevisionMetadata.kt
│   └── user
│       ├── Agreement.kt
│       ├── BasicUser.kt
│       ├── BasicUserRepository.kt
│       └── BasicUserSearchDto.kt
└── service
    └── auth
        ├── AuthService.kt
        ├── PasswordHashProvider.kt
        ├── RefreshTokenHandler.kt
        └── TokenProvider.kt
```

기본적인 사용자 모델과 인증 모델 및 Revision 모델을 제공합니다.
Revision 과 원활한 데이터 조회를 위해 '[Spring Data Commons](https://docs.spring.io/spring-data/commons/reference/index.html)'를
의존합니다.

엔티티 클래스를 작성 시 `EntityBase` 클래스를 상속받아 작성하며, `RepositoryBase` 인터페이스를 이용하여 레포지토리를 정의합니다.
레포지토리의 구현은 `infra` 모듈에서 제공하는 `JpaRepositoryHelper`, `MongoRepositoryHelper`, `RedisRepositoryHelper` 를 이용하여 구현하는 것을
권장합니다.

### infra

```
├── infra-auth-jpa
├── infra-auth-redis
├── infra-auth-spring-security
├── infra-jpa
├── infra-mongodb
└── infra-redis
```

JPA, mongodb, redis 를 지원하며, 각 모듈을 선택적으로 사용할 수 있습니다. 각
모듈은 '[Spring Data JPA](https://spring.io/projects/spring-data-jpa)', '[Spring Data MongoDB](https://spring.io/projects/spring-data-mongodb)', '[Spring Data Redis](https://spring.io/projects/spring-data-redis)'
를 의존하며, domain 에서 정의한 엔티티, 레포지토리와 호환을 위해 모델 및 Helper 클래스를 제공합니다.

트랜잭션, 동시성 처리를 위한 버전 정보 등과 같은 영속성 지식은 infra 모듈에서 전담합니다. 도메인 모듈에 영속정 지식을 노출하지 않기 위해 `DataModel`, `DocumentModel`,
`HashModel` 클래스와 `*RepositoryHelper` 클래스를 사용하는 것을 권장합니다.

```kotlin
// DataModel.kt
@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
@Audited
abstract class DataModel<T : EntityBase>(
    id: ByteArray,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) {
    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)", unique = true, nullable = false, updatable = false)
    var id: ByteArray = id
        protected set

    @CreatedDate
    var createdAt: LocalDateTime = createdAt
        protected set

    @LastModifiedDate
    var updatedAt: LocalDateTime = updatedAt
        protected set

    abstract fun toEntity(): T

    abstract fun update(entity: T)
}
```

```kotlin
// JpaRepositoryHelper.kt
@Transactional
abstract class JpaRepositoryHelper<E : EntityBase, D : DataModel<E>>(
    private val jpaRepository: JpaRepository<D, ByteArray>,
    private val entityClass: KClass<E>,
) : RepositoryBase<E> {
    // (...)
    
    override fun findAll(): List<E> = jpaRepository.findAll().map { it.toEntity() }

    override fun findAll(ids: List<BinaryId>): List<E> = jpaRepository.findAllById(ids.map { it.value }).map { it.toEntity() }

    override fun findAll(pageable: Pageable): Page<E> = jpaRepository.findAll(pageable).map { it.toEntity() }

    override fun update(
        id: BinaryId,
        modifier: (E) -> Unit,
    ) {
        val dataModel = jpaRepository.findById(id.value).orElseThrow { EntityNotFoundError(entityClass, id) }
        val entity = dataModel.toEntity()
        modifier.invoke(entity)
        dataModel.update(entity)
    }

    override fun delete(id: BinaryId) {
        jpaRepository.deleteById(id.value)
    }

    // (...)
}
```

### application

```text
├── EnableBallApplication.kt
├── lock
│   ├── DistributedLock.kt
│   ├── DistributedLockAop.kt
│   ├── LocalLockProvider.kt
│   └── LockProvider.kt
└── transaction
    └── TxAdvice.kt
```

'분산락'과 '트랜잭션'을 제공합니다.

#### DistributedLock

별도 설정이 없는 경우 `LocalLockProvider` 클래스에 구현된 spin-lock 을 이용하여 lock 을 제공합니다.
`infra-redis`모듈을 사용할 경우 `RedissonLockProvider`로 대체되며, '[Redisson](https://github.com/redisson/redisson)'에서 제공하는 pub/sub
방식을 이용한 락을 제공합니다.

##### 선언적 방법: use `@DistributedLock`

```kotlin
@GetMapping
@DistributedLock(key = "sample", waitTime = 7000, leaseTime = 5000)
fun get(): ResponseEntity<String> {
    Thread.sleep(3000)
    return ResponseEntity.ok("Hello World")
}
```

##### 프로그래밍적 방법: use `LockProvider::class`

```kotlin
@GetMapping
fun get(): ResponseEntity<String> =
    lockProvider.withLock("sample", waitTime = 7000, leaseTime = 5000) {
        Thread.sleep(3000)
        ResponseEntity.ok("Hello World")
    }
```

### presentation

```text
├── config
│   ├── JwtConfig.kt
│   └── SecurityConfig.kt
├── core
│   ├── EnableBallWebMvc.kt
│   ├── ErrorResponse.kt
│   ├── IsAnonymous.kt
│   ├── IsAuthorized.kt
│   └── ServletExtension.kt
├── handler
│   └── AuthController.kt
└── middleware
    ├── BallRequestFilter.kt
    ├── ErrorHandler.kt
    ├── JwtAuthenticationFilter.kt
    └── RequestLogger.k
```

JWT 토큰을 이용한 인증/인가 기능과 'AOP' 를 기반으로 한 로깅 및 오류 처리 기능을 제공합니다. 