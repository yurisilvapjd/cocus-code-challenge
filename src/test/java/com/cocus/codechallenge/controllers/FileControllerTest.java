package com.cocus.codechallenge.controllers;

import com.cocus.codechallenge.controllers.impl.FileControllerImpl;
import com.cocus.codechallenge.dtos.response.FileRandomLineResponse;
import com.cocus.codechallenge.services.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FileControllerImpl.class)
public class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private FileService fileService;

    private FileRandomLineResponse mockResponse;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockResponse = FileRandomLineResponse.builder()
                .withRandomLine("This is a random line from the file.")
                .build();
        when(fileService.getRandomLineFromLatestFile(anyString())).thenReturn(mockResponse);
    }

    @Test
    void testRandomLineJson() throws Exception {
        mockMvc.perform(get("/files/random-line")
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.randomLine").value("This is a random line from the file."));
    }

    @Test
    void testRandomLineXml() throws Exception {
        mockMvc.perform(get("/files/random-line")
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_XML))
                .andExpect(xpath("/FileRandomLineResponse/randomLine").string("This is a random line from the file."));
    }

    @Test
    void testRandomLinePlainText() throws Exception {
        mockMvc.perform(get("/files/random-line")
                        .header(HttpHeaders.ACCEPT, MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string("FileRandomLineResponse { randomLine='This is a random line from the file.' }"));
    }
}
