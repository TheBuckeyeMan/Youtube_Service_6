package com.example.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.LoggerFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;



public class ReadFileTest {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ReadFileTest.class);

    //Mock all the dependencies and all child objects of the class being tested 
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

    /*
     * testGetBasicFileContents_Success: Tests if we successfully recieved a response form the s3 bucket
     * testGetBasicFileContents_Null: Tests if we successfully recieved a response form the s3 bucket but the contents were null
     * testGetBasicFileContents_Fail: Tests if we failed to recieve a response form the s3 bucket
     */
    @Test
    void testGetBasicFileContents_Success(){
        String bucketName = "test-bucket";
        String key = "test-key";
        String expectedContent = "testContent";

        ResponseBytes mockResponseBytes = mock(ResponseBytes.class);
        when(mockResponseBytes.asByteArray()).thenReturn(expectedContent.getBytes(StandardCharsets.UTF_8));
        
        CompletableFuture<ResponseBytes> mockFuture = CompletableFuture.completedFuture(mockResponseBytes);
        when(s3Client.getObject(any(GetObjectRequest.class), any(AsyncResponseTransformer.class))).thenReturn(mockFuture);

        //Act
        String result = readFile.getBasicFileContents(bucketName, key);
        log.info("The result is: " + result);

        //Assersions are where the test is actually being conducted
        assertEquals(expectedContent, result);
        verify(s3Client, times(1)).getObject(any(GetObjectRequest.class), any(AsyncResponseTransformer.class));
        verifyNoInteractions(s3LoggingService);
    }

    @Test
    void testGetBasicFileContents_Null() {
        // Arrange
        String bucketName = "test-bucket";
        String key = "test-key";
    
        // Mocking the response to return null byte array
        ResponseBytes<GetObjectResponse> mockResponseBytes = mock(ResponseBytes.class);
        when(mockResponseBytes.asByteArray()).thenReturn(null);
    
        CompletableFuture<ResponseBytes<GetObjectResponse>> mockFuture = CompletableFuture.completedFuture(mockResponseBytes);
        when(s3Client.getObject(any(GetObjectRequest.class), any(AsyncResponseTransformer.class)))
                .thenReturn(mockFuture);
    
        // Act and Assert
        String result = readFile.getBasicFileContents(bucketName, key);
    
        // Assert the error message returned from the method
        assertTrue(result.contains("Error: Unable to read basic file contents"), "Expected error message for null content");
        verify(s3LoggingService, times(1)).logMessageToS3(anyString());
    }

    @Test
    void testGetBasicFileCOntents_Exception(){
        String bucketName = "test-bucket";
        String key = "test-key";

        ResponseBytes responseBytes = mock(ResponseBytes.class);
        when(s3Client.getObject(any(GetObjectRequest.class), any(AsyncResponseTransformer.class))).thenThrow(new RuntimeException("S3 Error"));

        //Act
        String result = readFile.getBasicFileContents(bucketName, key);

        //Assert
        assertTrue(result.contains("Error: Unable to read basic file contents"));
        verify(s3LoggingService, times(1)).logMessageToS3(anyString());

    }

    



    

}
