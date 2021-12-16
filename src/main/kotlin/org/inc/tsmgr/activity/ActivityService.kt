package org.inc.tsmgr.activity

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.inc.tsmgr.JsonExporter
import org.inc.tsmgr.activity.export.ExcelExporter
import org.inc.tsmgr.util.TimeUtils.Companion.parseDate
import org.slf4j.LoggerFactory
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import java.io.FileInputStream
import java.time.Month

@Service
class ActivityService(
    private val objectMapper: ObjectMapper,
    private val repository: CrudRepository<Activity, ActivityId>,
    private val excelExporter: ExcelExporter,
    private val jsonExporter: JsonExporter
) {
    private val LOG = LoggerFactory.getLogger(ActivityService::class.java)
    fun getActivities(): Iterable<Activity> = repository.findAll()

    fun getActivities(date: String): Iterable<Activity> = repository.findAll().filter { it.id.date.equals(date) }

    fun export(): String = getActivities()
        .let(this::toActivities)
        .let(excelExporter::exportActivities)
        .also { LOG.info(it) }

    fun exportJson(): String = getActivities()
        .let(this::toActivities)
        .let(jsonExporter::exportActivities)
        .also { LOG.info(it) }

    private fun toActivities(activities: Iterable<Activity>): Activities {
        return Activities(activities.groupBy { it.id.date.let(::parseDate).year }.mapValues { (year, activities) ->
            YearlyActivities(year,
                activities.groupBy { Month.of(it.id.date.let(::parseDate).month) }
                    .mapValues { (month, activities) ->
                        MonthlyActivities(month, activities.groupBy { it.id.date }.mapValues { (date, activities) ->
                            DailyActivities(date, activities.associateBy { it.id.accountId })
                        })
                    })
        })
    }

    fun saveActivity(activity: Activity) = repository.save(activity)

    fun deleteActivity(activity: Activity) = repository.deleteById(ActivityId(activity.date, activity.accountId))

    fun importActivities(activities: Activities): Iterable<Activity> =
        repository.saveAll(activities.yearlyActivities.values.flatMap { it.monthlyActivities.values }
            .flatMap { it.dailyActivities.values }.flatMap { it.activities.values })

    fun import(path: String): Iterable<Activity> = load(path).let(this::importActivities)


    private fun load(path: String): Activities =
        objectMapper.readValue(FileInputStream(path).readBytes().toString(Charsets.UTF_8))
}