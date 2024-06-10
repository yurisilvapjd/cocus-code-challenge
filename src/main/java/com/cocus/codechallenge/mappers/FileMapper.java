package com.cocus.codechallenge.mappers;

import com.cocus.codechallenge.dtos.response.FileLongestLinesResponse;
import com.cocus.codechallenge.dtos.response.FileRandomLineResponse;

import java.util.List;

public interface FileMapper {

    FileRandomLineResponse toRandomLineResponse(String fileName, Integer lineNumber, String randomLine,
                                                String mostFrequentLetter, String acceptHeader);

    FileRandomLineResponse toRandomLineBackwardsResponse(String randomLine);

    FileLongestLinesResponse toLongestLinesResponse(String fileName, List<String> lines);

}
