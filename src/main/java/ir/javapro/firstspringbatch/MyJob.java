package ir.javapro.firstspringbatch;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.database.JpaPagingItemReader;
import org.springframework.batch.infrastructure.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.infrastructure.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

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
        return persons->{
            personService.save((Chunk<Person>) persons);
        };
    }


    @Bean
    public ItemProcessor<Person, Person> processor() {
        return p -> {
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
                     ItemWriter<Person> jpaPersonWriter) {

        return new StepBuilder("step1", jobRepository)
                .<Person, Person>chunk(6)
                .reader(jpaPersonReader)
                .processor(processor())
                .writer(jpaPersonWriter)
                .transactionManager(transactionManager)
//                .faultTolerant()
//                .skip(RuntimeException.class)
//                .skipLimit(2)
                .build();
    }


    @Bean
    public Job job(JobRepository jobRepository,  Step step) {
        return new JobBuilder(jobRepository)
                .start(step)
                .build();
    }
}
