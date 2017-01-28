import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import grails.util.BuildSettings
import grails.util.Environment

String logPattern = "%level %date %logger - %msg%n"

// See http://logback.qos.ch/manual/groovy.html for details on configuration
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = logPattern
    }
}

root(INFO, ['STDOUT'])

def targetDir = BuildSettings.TARGET_DIR
if (Environment.isDevelopmentMode() && targetDir) {
    appender("FULL_STACKTRACE", FileAppender) {
        file = "${targetDir}/stacktrace.log"
        append = true
        encoder(PatternLayoutEncoder) {
            pattern = logPattern
        }
    }
    logger("StackTrace", ERROR, ['FULL_STACKTRACE'], false)
}
