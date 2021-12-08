package org.inc.tsmgr.activity

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ActivityRepository : MongoRepository<Activity, ActivityId>