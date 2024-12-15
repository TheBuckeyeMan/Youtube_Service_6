package com.example.app.service;

import java.io.File;
import java.io.FileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PrepFileForS3 {
    private static final Logger log = LoggerFactory.getLogger(PrepFileForS3.class);

    public File PrepFileForS3Upload(String videoPrompt){
        try{
            //Get the Temp Directory
            String fileName = "title.txt";
            String tempDir = "/tmp";

            //Create a new temp file
            File tempFile = new File(tempDir, fileName);
            log.info("The path of the temp file created based off the Title is: " + tempFile);

            //Write the contents of the file
            try (FileWriter writer = new FileWriter(tempFile)){
                writer.write(videoPrompt);
                writer.flush();
            }

            //Return The new Temp File
            log.info("The new Temp file was created Successfully!");
            return tempFile;

        } catch (Exception e){
            log.info("Error: Error on PrepFileForS3 line 35: We were unable to save to the Title file");
            throw new RuntimeException("Failed to Prepare Title File", e);
        }
    }
}