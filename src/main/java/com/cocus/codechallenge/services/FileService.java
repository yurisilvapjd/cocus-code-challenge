package com.cocus.codechallenge.services;

import com.cocus.codechallenge.dtos.response.FileLongestLinesResponse;
import com.cocus.codechallenge.dtos.response.FileRandomLineResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    void upload(MultipartFile fileData);

    FileRandomLineResponse getRandomLineFromLatestFile(String acceptHeader);

    FileRandomLineResponse getRandomLineBackwards();

    FileLongestLinesResponse getLongestOneHundredLines();

    FileLongestLinesResponse getLongestTwentyLines();
}
