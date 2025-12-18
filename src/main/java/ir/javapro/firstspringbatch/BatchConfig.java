package ir.javapro.firstspringbatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    //job
    //step

    @Bean
    public Job job(JobRepository jobRepository,
                   @Qualifier("printStep") Step printStep,
                   @Qualifier("printStep2") Step printStep2) {
        return new JobBuilder("print", jobRepository)
                .start(printStep)
                .next(printStep2)
//                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step printStep(JobRepository jobRepository) {
        return new StepBuilder(jobRepository)
                .tasklet((t1, t2) -> {
                    System.out.println("step 1");
                    String message = (String) t2.getStepContext().getJobParameters().get("message");
                    System.out.println(message);
                    return null;
                }).build();
    }

    @Bean
    public Step printStep2(JobRepository jobRepository) {
        return new StepBuilder(jobRepository)
                .tasklet((t1, t2) -> {
                    System.out.println("step 2");
                    return null;
                }).build();
    }


}
