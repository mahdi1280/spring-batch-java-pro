package ir.javapro.firstspringbatch;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.InvalidJobParametersException;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.launch.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/job")
public class JobController {

    private final Job job;
    private final JobOperator jobOperator;
    private final Job chunkJob;
    private final Job sampleJob;
    private final Job runPersonJob;

    public JobController(@Qualifier("job") Job job,
                         JobOperator jobOperator,
                         @Qualifier("chunkJob") Job chunkJob,
                         @Qualifier("sampleJob") Job sampleJob,
                         @Qualifier("personJob") Job runPersonJob) {
        this.job = job;
        this.jobOperator = jobOperator;
        this.chunkJob = chunkJob;
        this.sampleJob = sampleJob;
        this.runPersonJob = runPersonJob;
    }

    @GetMapping("/run")
    public void run(@RequestParam String message) throws JobInstanceAlreadyCompleteException, InvalidJobParametersException, JobExecutionAlreadyRunningException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis(), Boolean.TRUE)
                .addString("message", message)
                .toJobParameters();

        jobOperator.start(job, jobParameters);
    }

    @GetMapping("/runChunk")
    public void runChunk() throws JobInstanceAlreadyCompleteException, InvalidJobParametersException, JobExecutionAlreadyRunningException, JobRestartException {
        jobOperator.start(chunkJob, new JobParameters());
    }

    @GetMapping("/runSampleJob")
    public void runSampleJob() throws JobInstanceAlreadyCompleteException, InvalidJobParametersException, JobExecutionAlreadyRunningException, JobRestartException {
        jobOperator.start(sampleJob, new JobParameters());
    }

    @GetMapping("/runPersonJob")
    public void runPersonJob() throws JobInstanceAlreadyCompleteException, InvalidJobParametersException, JobExecutionAlreadyRunningException, JobRestartException {
        jobOperator.start(runPersonJob, new JobParameters());
    }
}
