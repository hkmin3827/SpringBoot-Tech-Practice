package springallinone.practice.batch.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.data.RepositoryItemReader;
import org.springframework.batch.infrastructure.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.Sort;
import springallinone.practice.batch.skipListener.BatchSkipListener;
import springallinone.practice.exceptionHandling.CustomException;
import springallinone.practice.jpa.entity.Member;
import springallinone.practice.jpa.repository.MemberRepository;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MemberMigrationJobConfig {

    private final JobRepository jobRepository;
    private final MemberRepository memberRepository;
    private final BatchSkipListener batchSkipListener;

    @Bean
    public Job memberMigrationJob() {
        return new JobBuilder("memberMigrationJob", jobRepository)
                .start(memberMigrationStep())
                .build();
    }

    @Bean
    public Step memberMigrationStep() {
        return new StepBuilder("memberMigrationStep", jobRepository)
                .<Member, Member>chunk(100)
                .reader(memberItemReader())
                .processor(memberItemProcessor())
                .writer(memberItemWriter())
                .faultTolerant()
                .skip(CustomException.class)
                .skipLimit(10)
                .retry(DataAccessResourceFailureException.class)
                .retryLimit(2)
                .listener(batchSkipListener)
                .build();
    }

    @Bean
    public RepositoryItemReader<Member> memberItemReader() {
        return new RepositoryItemReaderBuilder<Member>()
                .name("memberItemReader")
                .repository(memberRepository)
                .methodName("findAll")
                .sorts(Map.of("id", Sort.Direction.ASC))
                .pageSize(100)
                .build();
    }

    @Bean
    public ItemProcessor<Member, Member> memberItemProcessor() {
        return member -> {
            log.debug("Processing member: {}", member.getId());
            return member;
        };
    }

    @Bean
    public ItemWriter<Member> memberItemWriter() {
        return members -> members.forEach(member ->
                log.info("Migrated member: id= {}, email= {}", member.getId(), member.getEmail())
        );
    }
}
