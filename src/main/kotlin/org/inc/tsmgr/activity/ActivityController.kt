package org.inc.tsmgr.activity

import org.apache.logging.log4j.LogManager
import org.springframework.web.bind.annotation.*

@RestController()
@RequestMapping("/activity")
class ActivityController(
    private val service: ActivityService
) {
    private val LOG = LogManager.getLogger()

    @GetMapping("")
    fun getActivities(): Iterable<Activity> = service.getActivities()

    @PostMapping("/import")
    fun importActivities(@RequestBody activities: Activities): String = service.importActivities(activities)
        .count()
        .let { count -> "Successfully imported: $count activities".also { LOG.info(it) } }

    @PostMapping("/")
    fun saveActivity(@RequestBody activity: Activity): Activity = service.saveActivity(activity)

    @DeleteMapping("/")
    fun deleteActivity(@RequestBody activity: Activity) = service.deleteActivity(activity)

    @GetMapping("/{date}")
    fun getActivities(@RequestParam date: String): Iterable<Activity> = service.getActivities(date)

    @GetMapping("/export")
    fun export(): String = service.export()

    @PostMapping("/export/json")
    fun exportJson(): String = service.exportJson()
}