package com.example.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import software.amazon.awssdk.services.s3.S3AsyncClient;

public class ReadFileTest {

    //
    @Mock
    private S3AsyncClient s3Client;

    @Mock 
    private S3LoggingService s3LoggingService;

    @InjectMocks
    private ReadFile readFile;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    



    

}
