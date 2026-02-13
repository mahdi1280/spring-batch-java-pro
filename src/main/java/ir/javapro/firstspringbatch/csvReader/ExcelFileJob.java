package ir.javapro.firstspringbatch.csvReader;

import ir.javapro.firstspringbatch.model.PersonEntity;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class ExcelFileJob {



    @Bean
    public ItemReader<PersonEntity> excelReader() throws IOException {
        return new ExcelFileItemReader(new ClassPathResource("employee.xlsx"));
    }

    @Bean
    public ItemProcessor<PersonEntity, PersonEntity> excelProcessor() throws IOException {
        return p -> {
            if (p.getFirstName() == null) {
                return null;
            }
            return p;
        };
    }


//    @Bean
//    public ItemWriter<Person> excelWriter() throws IOException {
//        return p-> p.getItems().forEach(System.out::println);
//    }

    @Bean
    public JpaItemWriter<PersonEntity> excelWriter(EntityManagerFactory entityManagerFactory) throws IOException {
        return new JpaItemWriter<>(entityManagerFactory);
    }


    @Bean
    public Step excelStep(JobRepository jobRepository) throws IOException {
        return new StepBuilder(jobRepository)
                .<PersonEntity, PersonEntity>chunk(4)
                .reader(excelReader())
                .writer(excelWriter(null))
                .processor(excelProcessor())
                .build();

    }


    @Bean
    public Job excelJob(JobRepository jobRepository) throws IOException {
        return new JobBuilder(jobRepository)
                .start(excelStep(jobRepository))
                .build();
    }
}
