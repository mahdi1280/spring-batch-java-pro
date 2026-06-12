package ir.javapro.firstspringbatch;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class MyJob {

    public static final Integer CHUNK_SIZE = 4;

    @Bean
    public ItemReader<Person> jpaPersonReader() {
        List<Person> persons = Arrays.asList(
                new Person(1, "Ali"),
                new Person(2, "Reza"),
                new Person(3, "Sara"),
                new Person(4, "Mina")
        );

        return new ListItemReader<>(persons);
    }

    @Bean
    public ItemProcessor<Person, Person> processor() {
        return p -> {
            p.setName(p.getName() + "a");
            return p;
        };
    }

    @Bean
    public ItemWriter<Person> jpaPersonWriter() {
        return (persons) -> {
            for (Person person : persons) {
                System.out.println(person.getName());
            }
        };
    }

    @Bean
    public Step step(JobRepository jobRepository) {
        return new StepBuilder(jobRepository)
                .<Person, Person>chunk(CHUNK_SIZE)
                .reader(jpaPersonReader())
                .processor(processor())
                .writer(jpaPersonWriter())
                .build();
    }

    @Bean
    public Job job(JobRepository jobRepository) {
        return new JobBuilder(jobRepository)
                .start(step(null))
                .build();
    }
}