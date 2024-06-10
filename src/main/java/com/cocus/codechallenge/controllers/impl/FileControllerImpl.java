package com.cocus.codechallenge.controllers.impl;

import com.cocus.codechallenge.controllers.FileController;
import com.cocus.codechallenge.dtos.response.FileLongestLinesResponse;
import com.cocus.codechallenge.dtos.response.FileRandomLineResponse;
import com.cocus.codechallenge.services.FileService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileControllerImpl implements FileController {

    private final FileService fileService;

    public FileControllerImpl(FileService fileService) {
        this.fileService = fileService;
    }

    public void upload(MultipartFile file) {
        this.fileService.upload(file);
    }

    public FileRandomLineResponse randomLine(String acceptHeader) {
        return this.fileService.getRandomLineFromLatestFile(acceptHeader);
    }

    public FileRandomLineResponse randomLineBackwards() {
        return this.fileService.getRandomLineBackwards();
    }

    public FileLongestLinesResponse longestOneHundredLines() {
        return this.fileService.getLongestOneHundredLines();
    }

    public FileLongestLinesResponse longestTwentyLines() {
        return this.fileService.getLongestTwentyLines();
    }

}
