package com.example.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CreateTitle {
    private static final Logger log = LoggerFactory.getLogger(CreateTitle.class);
    private static final int MAX_LENGTH = 50;

    public String getTitle(String funFact){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(funFact);

            String title = rootNode.get(0).get("fact").asText();
            title = title.substring(0,MAX_LENGTH).trim() + "...  #Shorts  #Viral  #YouTubeShorts #fyp";

            //Ensure we dont exceed 99 Charictors
            if (title.length() > MAX_LENGTH){
                log.error("The title: " + title + " is to long and needs to be shortaned before we can upload.");
            }
            log.info("The title of the video will be: " + title);
            return title;
        } catch (Exception e){
            log.error("Error: Error Creating title. Line 29 of CreateTitle.java", e);
            return null;
        }
    }
}