package com.cocus.codechallenge.configurations.web;

import com.cocus.codechallenge.dtos.response.FileRandomLineResponse;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class PlainTextConverter implements HttpMessageConverter<FileRandomLineResponse> {

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return FileRandomLineResponse.class.isAssignableFrom(clazz) && MediaType.TEXT_PLAIN.includes(mediaType);
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return List.of(MediaType.TEXT_PLAIN);
    }

    @Override
    public FileRandomLineResponse read(Class<? extends FileRandomLineResponse> clazz, HttpInputMessage inputMessage) {
        return null;
    }

    @Override
    public void write(FileRandomLineResponse fileRandomLineResponse, MediaType contentType, HttpOutputMessage outputMessage) throws IOException {
        try (OutputStreamWriter writer = new OutputStreamWriter(outputMessage.getBody(), StandardCharsets.UTF_8)) {
            writer.write(fileRandomLineResponse.toString());
        }
    }
}
