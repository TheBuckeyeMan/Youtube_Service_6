package com.example.app.service;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ServiceTrigger {
    private static final Logger log = LoggerFactory.getLogger(ServiceTrigger.class);
    private ReadFile readFile;
    private CreateTitle createTitle;
    private PrepFileForS3 prepFileForS3;
    private PostFileToS3 postFileToS3;

    @Value("${spring.profiles.active}")
    private String environment;

    @Value("${aws.s3.bucket.landing}")
    private String landingBucket;

    @Value("${aws.s3.bucket.logging}")
    private String loggingBucket;

    @Value("${aws.s3.key.fun}")
    private String funFactKey;

    @Value("${aws.s3.key.title}")
    private String titleKey;

    @Value("${aws.s3.key.logging}")
    private String loggingKey;

    public ServiceTrigger(ReadFile readFile, CreateTitle createTitle, PrepFileForS3 prepFileForS3, PostFileToS3 postFileToS3){
        this.readFile = readFile;
        this.createTitle = createTitle;
        this.prepFileForS3 = prepFileForS3;
        this.postFileToS3 = postFileToS3;
    }

    public void TriggerService(){
        //Initialization Logs
        log.info("The Active Environment is set to: " + environment);
        log.info("Begining to Collect Contents of Fun Fact form S3 Bucket");

        //Trigger Services
        //1. Get Fun Fact from S3
        String funFact = readFile.getBasicFileContents(landingBucket, funFactKey);
        //2. Create Title
        String title = createTitle.getTitle(funFact);

        //3. Prep file for S3
        File titleFile = prepFileForS3.PrepFileForS3Upload(title);

        //4. Save Fun Fact to S3.
        postFileToS3.PostFileToS3Bucket(titleFile, landingBucket, titleKey);

        //Log Completion
        log.info("The Service has successfully complete and the Title file is saved in the " + titleKey + " Directory of the " + landingBucket + " Bucket!");
        log.info("Final: The Lambda has triggered successfully and the title is now saved in the S3 Bucket: " + landingBucket);

        //TODO Task 6: implement unit testing

        //TODO FIgure out how to havethis log to logging bucket without causing a failure 
    }
}