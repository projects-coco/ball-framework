package org.coco.infra.jpa

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.coco.core.type.BinaryId
import org.coco.domain.TestEntity
import org.coco.domain.TestEntityRepository
import org.coco.infra.jpa.model.TestEntityJpaRepository
import org.coco.infra.jpa.model.TestEntityRepositoryImpl
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate
import java.time.LocalDateTime
import java.util.concurrent.CountDownLatch

@ContextConfiguration(
    classes = [
        TestEntityRepositoryImpl::class,
    ],
)
@EnableJpaConfig(
    entityBasePackages = ["org.coco.infra.jpa"],
    repositoryBasePackages = ["org.coco.infra.jpa"],
)
class JpaRepositoryTest(
    private val testEntityRepository: TestEntityRepository,
    private val jpaRepository: TestEntityJpaRepository,
    private val tm: PlatformTransactionManager,
) : JpaTestSpecUsingMariadb() {
    init {
        var transaction: TransactionTemplate? = null
        this.beforeTest {
            transaction = TransactionTemplate(tm)
            transaction!!.propagationBehavior = TransactionTemplate.PROPAGATION_REQUIRES_NEW
        }
        this.test("TestEntityRepository.save()") {
            // given
            val testEntity = TestEntity(BinaryId.new(), LocalDateTime.now(), LocalDateTime.now(), "test")

            // when
            testEntityRepository.save(testEntity)

            // then
            testEntityRepository.findById(testEntity.id).get().payload shouldBe "test"
        }

        this.test("TestRepository.update()는 낙관적 락을 사용한다") {
            // given
            val numberOfThreads = 8
            val latch = CountDownLatch(numberOfThreads)
            val testEntity = TestEntity(BinaryId.new(), LocalDateTime.now(), LocalDateTime.now(), "test")
            transaction!!.execute {
                testEntityRepository.save(testEntity)
            }

            class EntityUpdateJob(
                private val id: BinaryId,
                private val payload: String,
                private val countDownLatch: CountDownLatch,
                private val delayInTransaction: Long = 0L,
            ) : Runnable {
                override fun run() {
                    println("Start Thread: $payload")
                    try {
                        testEntityRepository.update(id) {
                            println("Start Transaction: $payload")
                            Thread.sleep(delayInTransaction)
                            it.payload = payload
                        }
                        println("End Transaction with Success: $payload")
                    } catch (e: Exception) {
                        println("Exception In: $payload / $e")
                        println("End Transaction with Fail: $payload")
                    } finally {
                        countDownLatch.countDown()
                    }
                }
            }

            val jobs =
                listOf(EntityUpdateJob(testEntity.id, "updated-1", latch, 1000)) +
                    (2..numberOfThreads).map {
                        EntityUpdateJob(testEntity.id, "updated-$it", latch)
                    }

            // when
            jobs.forEach {
                Thread(it).start()
            }
            latch.await()

            // then
            testEntityRepository.findById(testEntity.id).get().payload shouldNotBe "updated-1"
            jpaRepository.findById(testEntity.id.value).get().version shouldBe 1
        }
    }
}
