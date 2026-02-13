package ir.javapro.firstspringbatch.csvReader;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class ExcelFileJob {



    @Bean
    public ItemReader<Person> excelReader() throws IOException {
        return new ExcelFileItemReader(new ClassPathResource("employee.xlsx"));
    }

    @Bean
    public ItemProcessor<Person, Person> excelProcessor() throws IOException {
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
    public JdbcBatchItemWriter<Person> excelWriter(DataSource dataSource) throws IOException {
        return new JdbcBatchItemWriterBuilder<Person>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO PERSON (id, firstName, lastName, email, salary) values(:id, :firstName, :lastName, :email, :salary)")
                .dataSource(dataSource)
                .build();
    }


    @Bean
    public Step excelStep(JobRepository jobRepository) throws IOException {
        return new StepBuilder(jobRepository)
                .<Person, Person>chunk(4)
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
