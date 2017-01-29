package smspanel


import org.quartz.Job
import org.quartz.JobExecutionContext

import java.util.concurrent.TimeUnit

class SendMessageJob implements Job {
    
    static triggers = {
        simple repeatInterval: TimeUnit.MILLISECONDS.convert(5, TimeUnit.SECONDS)
    }

    def concurrent = false
    
    SmsQueueService smsQueueService

    void execute(JobExecutionContext context) {
        smsQueueService.sendAllPendingMessages()
    }
}
