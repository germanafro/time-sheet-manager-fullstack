package org.inc.tsmgr.activity.export

import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.inc.tsmgr.activity.Activities
import org.inc.tsmgr.util.TimeUtils.Companion.parseDate
import org.inc.tsmgr.util.TimeUtils.Companion.weekDay
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileOutputStream

@Component
class ExcelExporter {

    val export_path = "export"
    private val pathname = "$export_path/time-sheet.xlsx"

    fun exportActivities(activities: Activities): String {

        return XSSFWorkbook()
            .let { workBook ->
                activities.yearlyActivities.entries.forEach { (year, yearlyActivities) ->
                    yearlyActivities.monthlyActivities.entries.forEach { (month, monthlyActivities) ->
                        workBook.createSheet("$year-$month").let { sheet ->
                            var i = 0
                            monthlyActivities.dailyActivities.entries.sortedBy { it.key.let(::parseDate) }
                                .forEach { (_, dailyActivities) ->
                                    dailyActivities.activities.values.forEach { activity ->
                                        sheet.createRow(i).let { row ->
                                            row.createCell(0).setCellValue(activity.id.date)
                                            row.createCell(1).setCellValue(activity.id.date.let(::weekDay).de)
                                            row.createCell(2).setCellValue(activity.id.accountId)
                                            row.createCell(3).setCellValue("")
                                            row.createCell(4).setCellValue("")
                                            row.createCell(5).setCellValue(activity.hours)
                                            row.createCell(6).setCellValue(activity.comment)
                                        }
                                        sheet.addMergedRegion(CellRangeAddress(i, i, 2, 4))
                                        i++
                                    }
                                }
                        }
                    }
                }
                File(export_path).mkdirs()
                File(pathname)
                    .also { file ->
                        FileOutputStream(file)
                            .also { workBook.write(it) }
                            .also { it.flush() }
                            .close()

                    }.absolutePath
            }
    }
}