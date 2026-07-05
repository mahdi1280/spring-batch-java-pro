package ir.javapro.firstspringbatch;

import jakarta.persistence.EntityManagerFactory;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.job.parameters.DefaultJobParametersValidator;
import org.springframework.batch.core.job.parameters.JobParametersValidator;
import org.springframework.batch.core.listener.ItemReadListener;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.batch.core.listener.StepExecutionListener;
import org.springframework.batch.core.partition.Partitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.StepExecution;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.database.JpaPagingItemReader;
import org.springframework.batch.infrastructure.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.infrastructure.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.infrastructure.item.support.SynchronizedItemStreamReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Configuration
public class MyJob {

    private final PersonService personService;
    private final DataSource dataSource;

    public MyJob(PersonService personService, DataSource dataSource) {
        this.personService = personService;
        this.dataSource = dataSource;
    }

    @Bean
    public Partitioner partitioner() {
        return gridSize -> {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            long min =  jdbcTemplate.queryForObject("select min(id) from person", Long.class);
            long max =  jdbcTemplate.queryForObject("select max(id) from person", Long.class);
            Map<String, ExecutionContext> result = new HashMap<>();
            long targetSize = (max - min) / gridSize + 1;
            long start = min;
            long end = start + targetSize + 1;
            for (int i = 0; i < gridSize; i++) {
                ExecutionContext context = new ExecutionContext();
                context.putLong("min", start);
                context.putLong("max", Math.min(end, max));
                result.put("p"+i, context);
                start +=targetSize;
                end+=targetSize;
            }
            return result;
        };
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Person> jpaPersonReader(EntityManagerFactory entityManagerFactory,
                                                       @Value("#{stepExecutionContext['min']}") Long min,
                                                       @Value("#{stepExecutionContext['max']}") Long max) {
        return new JpaPagingItemReaderBuilder<Person>()
                .name("personReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT p FROM Person p where p.id between :min and :max order by p.id")
                .parameterValues(Map.of("min", min, "max", max))
                .pageSize(100)
                .build();
    }

    @Bean
    public SynchronizedItemStreamReader<Person> synchronizedItemStreamReader(JpaPagingItemReader<Person> reader) {
        return new SynchronizedItemStreamReader<>(reader);
    }

    @Bean
    public ItemWriter<Person> jpaPersonWriter() {
        return persons -> {
            personService.save((Chunk<Person>) persons);
        };
    }

    @Bean
    public ItemProcessor<Person, Person> processor() {
        return p -> {
            Thread.sleep(10);
            personService.changeStatus(p);
            return p;
        };
    }

    @Bean
    public Step step(JobRepository jobRepository,
                     ItemProcessor<Person, Person> processor,
                     PlatformTransactionManager transactionManager,
                     JpaPagingItemReader<Person> jpaPersonReader,
                     SynchronizedItemStreamReader<Person> synchronizedItemStreamReader,
                     ItemWriter<Person> jpaPersonWriter, AsyncTaskExecutor taskExecutor) {
        return new StepBuilder("step1", jobRepository)
                .<Person, Person>chunk(100)
                .reader(jpaPersonReader)
                .processor(processor)
                .writer(jpaPersonWriter)
//                .taskExecutor(taskExecutor)
                .transactionManager(transactionManager)
                .build();
    }

    @Bean
    public Step managerStep(JobRepository jobRepository, Partitioner partitioner, Step step , AsyncTaskExecutor taskExecutor ) {
        return new StepBuilder("managerStep", jobRepository)
                .partitioner("worker", partitioner)
                .step(step)
                .gridSize(8)
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    public AsyncTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8);
        executor.setMaxPoolSize(16);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("batch-thread-");
        executor.initialize();
        return executor;
    }

    @Bean
    public Job job(JobRepository jobRepository, Step managerStep) {
        return new JobBuilder("job" ,jobRepository)
                .start(managerStep)
                .build();
    }
}
