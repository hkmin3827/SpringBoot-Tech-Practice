package springallinone.practice.batch.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemberMigrationScheduler {

    private final JobOperator jobOperator;
    private final Job memberMigrationJob;

    @Scheduled(cron = "0 0 0 * * MON", zone = "Asia/Seoul")
    public void runMemberMigrationJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("startTime", System.currentTimeMillis())
                    .toJobParameters();

            jobOperator.start(memberMigrationJob, jobParameters);
            log.info("MemberMigrationJob started successfully");
        } catch (Exception e) {
            log.error("MemberMigrationJob failed: {}", e.getMessage(), e);
        }
    }
}
