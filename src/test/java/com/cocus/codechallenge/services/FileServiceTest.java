package com.cocus.codechallenge.services;

import com.cocus.codechallenge.dtos.response.FileRandomLineResponse;
import com.cocus.codechallenge.exceptions.ResourceAlreadyExistsException;
import com.cocus.codechallenge.exceptions.ResourceNotFoundException;
import com.cocus.codechallenge.mappers.impl.FileMapperImpl;
import com.cocus.codechallenge.models.File;
import com.cocus.codechallenge.repositories.FileRepository;
import com.cocus.codechallenge.services.impl.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class FileServiceTest {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private FileServiceImpl fileService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fileService = new FileServiceImpl(fileRepository, new FileMapperImpl());
    }


    @Test
    void upload_should_save_file_successfully() throws IOException {
        when(multipartFile.getOriginalFilename()).thenReturn("testfile.txt");
        when(multipartFile.getBytes()).thenReturn(new byte[0]);
        when(fileRepository.save(anyString(), any(byte[].class))).thenReturn(true);

        this.fileService.upload(multipartFile);

        verify(fileRepository, times(1)).save("testfile.txt", new byte[0]);
    }

    @Test
    void upload_should_throw_exception_when_file_already_exists() throws IOException {
        when(multipartFile.getOriginalFilename()).thenReturn("testfile.txt");
        when(multipartFile.getBytes()).thenReturn(new byte[0]);
        when(fileRepository.save(anyString(), any(byte[].class))).thenReturn(false);

        assertThrows(ResourceAlreadyExistsException.class, () -> this.fileService.upload(multipartFile));
    }

    @Test
    void upload_should_throw_exception_when_operation_fails() throws IOException {
        when(multipartFile.getOriginalFilename()).thenReturn("testfile.txt");
        when(multipartFile.getBytes()).thenThrow(new IOException("IO Exception"));

        assertThrows(RuntimeException.class, () -> this.fileService.upload(multipartFile));
    }

    @ParameterizedTest
    @ValueSource(strings = {"text/plain", "application/json", "application/xml", "application/*"})
    void get_random_line_from_latest_file_should_return_expected_response(String acceptHeader) {
        var expectedFileName = "test-file.txt";
        var expectedFileData = "abcdefghijklmnopqrstuvwyxz".getBytes();
        var file = new File(expectedFileName, expectedFileData);

        when(fileRepository.getLatest()).thenReturn(Optional.of(file));

        var result = this.fileService.getRandomLineFromLatestFile(acceptHeader);

        assertInstanceOf(FileRandomLineResponse.class, result);
        if (acceptHeader.equals("application/*")) {
            assertEquals(expectedFileName, result.getFileName());
            assertEquals(1, result.getLineNumber());
            assertNull(result.getRandomLineBackwards());
            assertEquals("a", result.getMostFrequentLetter());
        } else {
            assertEquals(new String(expectedFileData), result.getRandomLine());
            assertNull(result.getFileName());
            assertNull(result.getLineNumber());
            assertNull(result.getRandomLineBackwards());
            assertNull(result.getMostFrequentLetter());
        }
    }

    @Test
    void get_random_line_from_latest_file_should_Throw_exception_when_no_files_found() {
        when(fileRepository.getLatest()).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> this.fileService.getRandomLineFromLatestFile("application/json"));
    }

    @Test
    void get_random_line_backwards_should_return_expected_response() {
        var expectedFileName = "test-file.txt";
        var fileData = "abcdefghijklmnopqrstuvwyxz".getBytes();
        var file = new File(expectedFileName, fileData);

        when(fileRepository.getRandom()).thenReturn(Optional.of(file));

        var result = this.fileService.getRandomLineBackwards();

        verify(fileRepository, times(1)).getRandom();
        assertInstanceOf(FileRandomLineResponse.class, result);
        assertEquals("zxywvutsrqponmlkjihgfedcba", result.getRandomLineBackwards());
        assertNull(result.getFileName());
        assertNull(result.getLineNumber());
        assertNull(result.getRandomLine());
        assertNull(result.getMostFrequentLetter());
    }

    @Test
    void get_random_line_backwards_should_exception_when_no_file_is_found() {
        when(fileRepository.getRandom()).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> this.fileService.getRandomLineBackwards());
    }

    @Test
    void get_longest_one_hundred_lines_should_return_expected_response() throws IOException {

        List<String> fileNames = List.of("file-1.txt", "file-2.txt", "file-3.txt", "file-4.txt");
        List<File> files = loadFilesFromResources(fileNames);

        when(fileRepository.getAll()).thenReturn(files);

        var result = fileService.getLongestOneHundredLines();

        verify(fileRepository).getAll();
        assertEquals(result.getLongestLines().size(), 100);
        assertEquals("All files included", result.getFilename());
        result.getLongestLines().forEach(line -> assertTrue(line.contains("longest-line")));
    }

    @Test
    void get_longest_twenty_lines_should_return_expected_response() throws IOException {

        List<String> fileNames = List.of("file-4.txt");
        List<File> files = loadFilesFromResources(fileNames);

        when(fileRepository.getRandom()).thenReturn(Optional.of(files.get(0)));

        var result = fileService.getLongestTwentyLines();

        verify(fileRepository).getRandom();
        assertEquals(20, result.getLongestLines().size());
        assertEquals("file-4.txt", result.getFilename());
        result.getLongestLines().forEach(line -> assertTrue(line.contains("file-4-longest-line")));
    }


    /**
     * Private Methods
     */

    public static List<File> loadFilesFromResources(List<String> fileNames) {
        return fileNames.stream()
                .map(fileName -> {
                    try {
                        Path path = Paths.get(ClassLoader.getSystemResource(fileName).toURI());
                        byte[] fileData = Files.readAllBytes(path);
                        return new File(fileName, fileData);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to load file: " + fileName, e);
                    }
                })
                .toList();
    }
}
