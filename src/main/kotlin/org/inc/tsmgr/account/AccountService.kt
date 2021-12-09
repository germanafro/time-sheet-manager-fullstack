package org.inc.tsmgr.account

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.logging.log4j.LogManager.getLogger
import org.inc.tsmgr.JsonExporter
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import java.io.FileInputStream

@Service
class AccountService(
    private val objectMapper: ObjectMapper,
    private val repository: CrudRepository<Account, String>,
    private val jsonExporter: JsonExporter
) {
    private val LOG = getLogger()

    fun getAccounts(): Iterable<Account> = repository.findAll()

    fun getAccount(id: String): Account? = repository.findById(id).orElse(null)

    fun import(path: String): String =
        load(path)
            .let(::import)

    fun import(accounts: Accounts): String = accounts
        .let(Accounts::accounts).values
        .map { repository.save(it) }
        .count()
        .let { count -> "imported: $count accounts".also { LOG.info(it) } }

    fun exportJson(): String = getAccounts()
        .associateBy(Account::id)
        .let(::Accounts)
        .let(jsonExporter::exportAccounts)
        .also { LOG.info(it) }


    private fun load(path: String): Accounts =
        objectMapper.readValue(FileInputStream(path).readBytes().toString(Charsets.UTF_8))


}