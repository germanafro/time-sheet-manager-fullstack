package org.inc.tsmgr.account

import org.springframework.data.mongodb.repository.MongoRepository

interface AccountRepository : MongoRepository<Account, String> {
}