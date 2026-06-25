package ir.javapro.firstspringbatch;

import jakarta.persistence.EntityManagerFactory;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.DefaultJobParametersValidator;
import org.springframework.batch.core.job.parameters.JobParametersValidator;
import org.springframework.batch.core.listener.ItemReadListener;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.batch.core.listener.StepExecutionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.StepExecution;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.database.JpaPagingItemReader;
import org.springframework.batch.infrastructure.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.infrastructure.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.Objects;

@Configuration
public class MyJob {

    private final PersonService personService;

    public MyJob(PersonService personService) {
        this.personService = personService;
    }

    @Bean
    public JpaPagingItemReader<Person> jpaPersonReader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<Person>()
                .name("personReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT p FROM Person p order by p.id")
                .pageSize(6)

                .build();
    }

    @Bean
    public ItemWriter<Person> jpaPersonWriter() {
        return persons -> {
            personService.save((Chunk<Person>) persons);
        };
    }


    @Bean
    @StepScope
    public ItemProcessor<Person, Person> processor(@Value("#{jobParameters['id']}") Long id) {
        return p -> {
            System.out.println(id);
//            if(p.getName().equals("Tara")) {
//                throw new RuntimeException("error");
//            }
            personService.changeStatus(p);
            return p;
        };
    }

    @Bean
    public Step step(JobRepository jobRepository,
                     PlatformTransactionManager transactionManager,
                     JpaPagingItemReader<Person> jpaPersonReader,
                     ItemWriter<Person> jpaPersonWriter, StepExecutionListener listener) {

        return new StepBuilder("step1", jobRepository)
                .<Person, Person>chunk(6)
                .reader(jpaPersonReader)
                .processor(processor(null))
                .writer(jpaPersonWriter)
                .transactionManager(transactionManager)
//                .faultTolerant()
//                .skip(RuntimeException.class)
//                .skipLimit(2)
                .listener(listener)
                .build();
    }


    @Bean
    public Job job(JobRepository jobRepository, Step step, JobParametersValidator validator, JobExecutionListener listener) {
        return new JobBuilder(jobRepository)
                .start(step)
                .validator(validator)
                .listener(listener)
                .build();
    }

    @Bean
    public JobParametersValidator validator() {
        return (parameters) -> {
            Long id = parameters.getLong("id");
            if (Objects.isNull(id) || id == 0) {
                throw new RuntimeException("id is null or id is 0");
            }
        };
    }

    @Component
    public static class JobListener implements JobExecutionListener {
        @Override
        public void beforeJob(JobExecution jobExecution) {
            System.out.println("before job");
        }

        @Override
        public void afterJob(JobExecution jobExecution) {
            System.out.println("after job");
        }
    }

    @Component
    public static class StepListener implements StepExecutionListener{
        @Override
        public void beforeStep(StepExecution stepExecution) {
            System.out.println("before step");
            StepExecutionListener.super.beforeStep(stepExecution);
        }

        @Override
        public @Nullable ExitStatus afterStep(StepExecution stepExecution) {
            System.out.println("after step");
            return StepExecutionListener.super.afterStep(stepExecution);
        }

    }


}
