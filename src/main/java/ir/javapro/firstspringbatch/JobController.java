package ir.javapro.firstspringbatch;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.InvalidJobParametersException;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobController {

    private final JobOperator jobOperator;
    private final Job job;

    public JobController(JobOperator jobOperator, Job job) {
        this.jobOperator = jobOperator;
        this.job = job;
    }

    @GetMapping("/start")
    public ResponseEntity<?> startJob() throws JobInstanceAlreadyCompleteException, InvalidJobParametersException, JobExecutionAlreadyRunningException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis(), Boolean.TRUE)
                .addLong("id", 1L)
                .toJobParameters();
        jobOperator.start(job, jobParameters);
        return ResponseEntity.ok().build();
    }

}
