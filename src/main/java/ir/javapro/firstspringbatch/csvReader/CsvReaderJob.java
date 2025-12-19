package ir.javapro.firstspringbatch.csvReader;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class CsvReaderJob {


    @Bean
    public FlatFileItemReader<Person> personReader() {
        return new FlatFileItemReaderBuilder<Person>()
                .name("personReader")
                .resource(new ClassPathResource("person.csv"))
                .linesToSkip(1)
                .delimited()
                .delimiter(",")
                .names("id", "firstName", "lastName", "email", "salary")
                .fieldSetMapper(fieldSet -> new Person(
                        fieldSet.readLong("id"),
                        fieldSet.readString("firstName"),
                        fieldSet.readString("lastName"),
                        fieldSet.readString("email"),
                        fieldSet.readLong("salary"))
                ).build();
    }

    @Bean
    public ItemProcessor<Person, Person> personProcessor() {
        return person -> {
            person.setSalary(person.getSalary() + 1000);
            return person;
        };
    }

    @Bean
    public ItemWriter<Person> personWrite() {
        return personList -> {
            personList.forEach(person -> {
                System.out.println(person);
            });
        };
    }

    @Bean
    public Job personJob(JobRepository jobRepository, @Qualifier("personStep") Step personStep) {
        return new JobBuilder(jobRepository)
                .start(personStep)
                .build();
    }

    @Bean
    public Step personStep(JobRepository jobRepository) {
        return new StepBuilder(jobRepository)
                .<Person, Person>chunk(2)
                .reader(personReader())
                .processor(personProcessor())
                .writer(personWrite())
                .build();
    }

}
