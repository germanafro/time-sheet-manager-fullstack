package org.inc.tsmgr

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("application")
class ApplicationProperties {
    var data: Data = Data()

    inner class Data {
        var json: Boolean = false
    }
}