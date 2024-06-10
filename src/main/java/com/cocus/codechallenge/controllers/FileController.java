package com.cocus.codechallenge.controllers;

import com.cocus.codechallenge.dtos.response.FileLongestLinesResponse;
import com.cocus.codechallenge.dtos.response.FileRandomLineResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Files", description = "File Management API")
@RequestMapping("files")
public interface FileController {

    @Operation(summary = "Uploads a text File")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "successful operation")})
    @PostMapping(
            path = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    void upload(@Parameter(description = "File to upload") @RequestPart(value = "file") MultipartFile file);

    @Operation(summary = "Returns one random line of the previously uploaded File")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = {
                            @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class)),
                            @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)),
                            @Content(mediaType = "application/xml", schema = @Schema(implementation = String.class)),
                            @Content(mediaType = "application/*", schema = @Schema(implementation = String.class))
                    })
    })
    @GetMapping(value = "/random-line", produces = {"application/json", "text/plain", "application/xml"})
    FileRandomLineResponse randomLine(@Parameter(hidden = true) @RequestHeader(HttpHeaders.ACCEPT) String acceptHeader);

    @Operation(summary = "Returns one random line of a random File backwards")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "successful operation")})
    @GetMapping("/random-line-backwards")
    FileRandomLineResponse randomLineBackwards();

    @Operation(summary = "Returns the 100 longest lines of all Files uploaded")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "successful operation")})
    @GetMapping("/longest-100-lines")
    FileLongestLinesResponse longestOneHundredLines();

    @Operation(summary = "Return the 20 longest lines of a random File")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "successful operation")})
    @GetMapping("/longest-20-lines")
    FileLongestLinesResponse longestTwentyLines();
}
