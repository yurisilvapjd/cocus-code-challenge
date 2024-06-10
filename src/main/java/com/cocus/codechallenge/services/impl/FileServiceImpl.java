package com.cocus.codechallenge.services.impl;


import com.cocus.codechallenge.dtos.response.FileLongestLinesResponse;
import com.cocus.codechallenge.dtos.response.FileRandomLineResponse;
import com.cocus.codechallenge.exceptions.ResourceAlreadyExistsException;
import com.cocus.codechallenge.exceptions.ResourceNotFoundException;
import com.cocus.codechallenge.mappers.FileMapper;
import com.cocus.codechallenge.models.File;
import com.cocus.codechallenge.repositories.FileRepository;
import com.cocus.codechallenge.services.FileService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    private final FileMapper fileMapper;

    public FileServiceImpl(@Qualifier("inMemoryFileRepository") FileRepository fileRepository, FileMapper fileMapper) {
        this.fileRepository = fileRepository;
        this.fileMapper = fileMapper;
    }

    @Override
    public void upload(MultipartFile fileData) {
        try {
            if (!this.fileRepository.save(fileData.getOriginalFilename(), fileData.getBytes()))
                throw new ResourceAlreadyExistsException("File already exists");
        } catch (IOException e) {
            throw new RuntimeException("Fail to read File");
        }

    }

    @Override
    public FileRandomLineResponse getRandomLineFromLatestFile(String acceptHeader) {
        var latestFile = this.fileRepository.getLatest()
                .orElseThrow(() -> new ResourceNotFoundException("No Files found"));
        var randomIndexAndLine = this.getRandomLine(latestFile.getData());
        var mostFrequentChar = this.getMostFrequentChar(randomIndexAndLine.getValue());
        return this.fileMapper.toRandomLineResponse(latestFile.getName(), randomIndexAndLine.getKey(),
                randomIndexAndLine.getValue(), mostFrequentChar, acceptHeader);
    }

    @Override
    public FileRandomLineResponse getRandomLineBackwards() {
        var randomFile = this.fileRepository.getRandom()
                .orElseThrow(() -> new ResourceNotFoundException("No Files found"));
        var randomIndexAndLine = this.getRandomLine(randomFile.getData());
        return this.fileMapper.toRandomLineBackwardsResponse(revert(randomIndexAndLine.getValue()));
    }

    @Override
    public FileLongestLinesResponse getLongestOneHundredLines() {
        var allFiles = this.fileRepository.getAll();
        if (allFiles == null || allFiles.isEmpty()) throw new ResourceNotFoundException("No Files found");
        var longestOneHundredLines = allFiles.stream()
                .map(this::getAllLines)
                .flatMap(Collection::stream)
                .sorted((o1, o2) -> Integer.compare(o2.length(), o1.length()))
                .limit(100)
                .toList();
        return this.fileMapper.toLongestLinesResponse("All files included", longestOneHundredLines);
    }

    @Override
    public FileLongestLinesResponse getLongestTwentyLines() {
        var randomFile = this.fileRepository.getRandom()
                .orElseThrow(() -> new ResourceNotFoundException("No Files found"));
        var longestTwentyLines = this.getAllLines(randomFile).stream()
                .sorted((o1, o2) -> Integer.compare(o2.length(), o1.length()))
                .limit(20)
                .toList();
        return this.fileMapper.toLongestLinesResponse(randomFile.getName(), longestTwentyLines);
    }

    /**
     * Private Methods
     */

    private Map.Entry<Integer, String> getRandomLine(byte[] byteArray) {
        var content = new String(byteArray, StandardCharsets.UTF_8);
        var lines = content.split("\r?\n");
        int randomIndex = new Random().nextInt(lines.length);
        var randomLine = lines[randomIndex];
        return Map.entry(randomIndex + 1, randomLine);
    }

    private String getMostFrequentChar(String value) {
        int[] charFrequency = new int[1112064];
        for (int i = 0; i < value.length(); i++) {
            charFrequency[value.charAt(i)]++;
        }
        int mostFrequentIndex = 0;
        int mostFrequentValue = 0;
        for (int i = 0; i < charFrequency.length; i++) {
            if (charFrequency[i] > mostFrequentValue) {
                mostFrequentIndex = i;
                mostFrequentValue = charFrequency[i];
            }
        }
        return String.valueOf((char) mostFrequentIndex);
    }

    private List<String> getAllLines(File file) {
        var content = new String(file.getData(), StandardCharsets.UTF_8);
        var lines = content.split("\r?\n");
        return Arrays.stream(lines).toList();
    }

    private String revert(String value) {
        var charArr = value.toCharArray();
        var head = 0;
        var tail = value.length() - 1;
        while (head < tail) {
            var aux = charArr[head];
            charArr[head] = charArr[tail];
            charArr[tail] = aux;
            head++;
            tail--;
        }
        return String.valueOf(charArr);
    }

}
