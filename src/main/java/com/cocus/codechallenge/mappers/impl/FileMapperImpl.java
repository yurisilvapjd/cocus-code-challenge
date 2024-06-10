package com.cocus.codechallenge.mappers.impl;

import com.cocus.codechallenge.dtos.response.FileLongestLinesResponse;
import com.cocus.codechallenge.dtos.response.FileRandomLineResponse;
import com.cocus.codechallenge.mappers.FileMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileMapperImpl implements FileMapper {

    @Override
    public FileRandomLineResponse toRandomLineResponse(String fileName, Integer lineNumber, String randomLine,
                                                       String mostFrequentLetter, String acceptHeader) {
        if (acceptHeader.equals("application/*")) {
            return FileRandomLineResponse.builder()
                    .withFileName(fileName)
                    .withLineNumber(lineNumber)
                    .withRandomLine(randomLine)
                    .withMostFrequentLetter(mostFrequentLetter)
                    .build();
        }
        return FileRandomLineResponse.builder()
                .withRandomLine(randomLine)
                .build();
    }

    @Override
    public FileRandomLineResponse toRandomLineBackwardsResponse(String randomLine) {
        return FileRandomLineResponse.builder()
                .withRandomLineBackwards(randomLine)
                .build();
    }

    @Override
    public FileLongestLinesResponse toLongestLinesResponse(String fileName, List<String> lines) {
        return FileLongestLinesResponse.builder()
                .withFilename(fileName)
                .withLongestLines(lines)
                .build();
    }

}
