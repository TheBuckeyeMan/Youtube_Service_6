package com.example.app.service;

import java.io.File;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class PostFileToS3 {
    private static final Logger log = LoggerFactory.getLogger(PostFileToS3.class);
    private final S3Client s3Client;
    
    public PostFileToS3(S3Client s3Client){
        this.s3Client = s3Client;
    }

    public void PostFileToS3Bucket(File S3File, String landingBucket, String titleKey){
        try{
            //Verify file Exists
            if (!S3File.exists()){
                log.error("Error: Error on PostFileToS3.java. Title File Does not Exist");
            }

            //Create the Put Object Request
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                                                .bucket(landingBucket)
                                                                .key(titleKey)
                                                                .build();
            
            //Upload the file
            s3Client.putObject(putObjectRequest, RequestBody.fromFile(S3File));
            log.info("Title File has been successfully saved to the " + landingBucket + " Bucket!");
        } catch (Exception e){
            log.error("Error: Error on PostFileToS3 - uploading the Title File to the S3 Bucket has failed. Line 41", e.getMessage(),e);
            throw new RuntimeException("Failed To Upload file to S3", e);
        }  
    }
}