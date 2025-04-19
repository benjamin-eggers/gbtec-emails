package de.beg.gbtec.emails.scheduled;

import de.beg.gbtec.emails.service.EmailService;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class SpamScheduler {

    private final Logger log = LoggerFactory.getLogger(SpamScheduler.class);
    private final EmailService emailService;

    public SpamScheduler(EmailService emailService) {
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 0 10 * * *")
    @SchedulerLock(name = "spam-scheduler")
    public void scheduledSpamRun() {
        log.info("[SPAM-RUN] Start of scheduled invocation");
        try {
            emailService.markEmailsAsSpam();
        } catch (Exception e) {
            log.error("[SPAM-RUN] An error occurred during scheduled invocation", e);
        }
        log.info("[SPAM-RUN] End of scheduled invocation");
    }

}
