package org.inc.tsmgr

import org.apache.logging.log4j.LogManager
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import org.testcontainers.containers.BindMode.READ_WRITE
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName
import java.io.File

@SpringBootApplication
@EnableMongoRepositories
@ConfigurationPropertiesScan
class TSMgr {
    companion object {
        private val LOG = LogManager.getLogger()
        private val DATA_PATH = "local/mongo/data"
        private val CONTAINER_DATA_PATH = "/data/db"
        private val LOGS_PATH = "local/mongo/logs"
        val mongoDB: GenericContainer<*> = GenericContainer(DockerImageName.parse("mongo:4.0.10"))
            .withClasspathResourceMapping(
                ClassLoader.getSystemClassLoader().getResource("").path
                    .let { "$it$DATA_PATH" }
                    .let { File(it) }
                    .also { it.mkdirs() }
                    .let { DATA_PATH },
                CONTAINER_DATA_PATH,
                READ_WRITE
            )
            .withExposedPorts(27017)
            .also { it.start() }
            .also { System.setProperty("spring.data.mongodb.host", it.host) }
            .also { System.setProperty("spring.data.mongodb.port", it.getMappedPort(27017).toString()) }
            .also { LOG.info("started MongoDB on : ${it.host}:${it.getMappedPort(27017)}") }
    }
}

fun main(args: Array<String>) {


    runApplication<TSMgr>(*args)
}
