package com.cocus.codechallenge.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true, setterPrefix = "with", builderClassName = "Builder")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileLongestLinesResponse {

    private String filename;

    private List<String> longestLines;

}
