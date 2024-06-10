package com.cocus.codechallenge.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Builder(toBuilder = true, setterPrefix = "with", builderClassName = "Builder")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileRandomLineResponse {

    private String fileName;

    private Integer lineNumber;

    private String randomLine;

    private String randomLineBackwards;

    private String mostFrequentLetter;

    /**
     * Overrides
     */

    @Override
    public String toString() {
        return "FileRandomLineResponse {" +
                (Objects.nonNull(fileName) ? (" fileName='" + fileName + "'") : "") +
                (Objects.nonNull(lineNumber) ? (" lineNumber=" + lineNumber + "'") : "") +
                (Objects.nonNull(randomLine) ? (" randomLine='" + randomLine + "'") : "") +
                (Objects.nonNull(randomLineBackwards) ? (" randomLineBackwards='" + randomLineBackwards + "'") : "") +
                (Objects.nonNull(mostFrequentLetter) ? (" mostFrequentLetter='" + mostFrequentLetter + "'") : "") + " }";
    }
}
