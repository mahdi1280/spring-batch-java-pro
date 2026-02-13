package ir.javapro.firstspringbatch.job;

import ir.javapro.firstspringbatch.model.Person;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidPersonJobWithJpa {

    public static final Integer CHUNK_SIZE = 4;

    @Bean
    public JpaPagingItemReader<Person> jpaPersonReader(EntityManagerFactory emf) {
        JpaPagingItemReader<Person> reader = new JpaPagingItemReader<>(emf);

        reader.setQueryString("""
                select p from Person p where p.valid is null
                """);
        reader.setPageSize(CHUNK_SIZE);
        reader.setSaveState(true);
        return reader;
    }

    @Bean
    public ItemProcessor<Person, Person> processor() {
        return p -> {
            p.setValid(Boolean.TRUE);
            return p;
        };
    }

    @Bean
    public JpaItemWriter<Person> jpaPersonWriter(EntityManagerFactory emf) {
        return new JpaItemWriter<>(emf);
    }

    @Bean
    public Step step(JobRepository jobRepository) {
        return new StepBuilder(jobRepository)
                .<Person, Person>chunk(CHUNK_SIZE)
                .reader(jpaPersonReader(null))
                .processor(processor())
                .writer(jpaPersonWriter(null))
                .build();
    }

    @Bean
    public Job job(JobRepository jobRepository) {
        return new JobBuilder(jobRepository)
                .start(step(null))
                .build();

    }
}
