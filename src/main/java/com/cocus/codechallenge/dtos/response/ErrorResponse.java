package com.cocus.codechallenge.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true, setterPrefix = "with", builderClassName = "Builder")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private Integer status;

    private String message;

    private final long timestamp = System.currentTimeMillis();

}
