//package ir.javapro.firstspringbatch.job;
//
//import ir.javapro.firstspringbatch.model.Person;
//import org.springframework.batch.core.job.Job;
//import org.springframework.batch.core.job.builder.JobBuilder;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.step.Step;
//import org.springframework.batch.core.step.builder.StepBuilder;
//import org.springframework.batch.infrastructure.item.ItemProcessor;
//import org.springframework.batch.infrastructure.item.database.BeanPropertyItemSqlParameterSourceProvider;
//import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
//import org.springframework.batch.infrastructure.item.database.JdbcCursorItemReader;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.core.BeanPropertyRowMapper;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class ValidaPersonJob {
//
//    @Bean
//    public JdbcCursorItemReader<Person>  jdbcPersonItemReader(DataSource dataSource) {
//        String sql = """
//                   SELECT * from person where valid is null;
//                """;
//        return new JdbcCursorItemReader<>(dataSource,
//                sql,
//                new BeanPropertyRowMapper<>(Person.class));
//
//    }
//
//    @Bean
//    public ItemProcessor<Person, Person> itemProcessor() {
//        return person -> {
//            person.setValid(Boolean.TRUE);
//            return person;
//        };
//    }
//
//    @Bean
//    public JdbcBatchItemWriter<Person>  jdbcPersonItemWriter(DataSource dataSource) {
//        JdbcBatchItemWriter<Person>  jdbcPersonItemWriter = new JdbcBatchItemWriter<>();
//        jdbcPersonItemWriter.setDataSource(dataSource);
//        jdbcPersonItemWriter.setSql("""
//                    update person set valid = :valid where id = :id;
//                """);
//        jdbcPersonItemWriter.setItemSqlParameterSourceProvider(
//                new BeanPropertyItemSqlParameterSourceProvider<>()
//        );
//        return jdbcPersonItemWriter;
//    }
//
//    @Bean
//    public Step step(JobRepository jobRepository) {
//        return new StepBuilder(jobRepository)
//                .<Person, Person>chunk(4)
//                .reader(jdbcPersonItemReader(null))
//                .processor(itemProcessor())
//                .writer(jdbcPersonItemWriter(null))
//                .build();
//    }
//
//    @Bean
//    public Job job(JobRepository jobRepository) {
//        return new JobBuilder(jobRepository)
//                .start(step(jobRepository))
//                .build();
//    }
//}
