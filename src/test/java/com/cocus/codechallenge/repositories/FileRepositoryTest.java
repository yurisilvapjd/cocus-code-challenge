package com.cocus.codechallenge.repositories;

import com.cocus.codechallenge.models.File;
import com.cocus.codechallenge.repositories.impl.inmemory.InMemoryFileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileRepositoryTest {

    private InMemoryFileRepository fileRepository;

    private LinkedList<File> fileStore;

    @BeforeEach
    void setUp() {
        this.fileStore = new LinkedList<>();
        this.fileRepository = new InMemoryFileRepository(this.fileStore);
    }

    @Test
    void save_should_succeed() {
        var fileName = "file-1.txt";
        var fileData = "This is file 1 content".getBytes();
        var newFile = new File(fileName, fileData);

        var result = this.fileRepository.save(fileName, fileData);

        assertTrue(result);
        assertTrue(this.fileStore.contains(newFile));
    }

    @Test
    void save_should_fail_when_file_already_exists() {
        var fileName = "file-2.txt";
        var fileData = "This is file 2 content".getBytes();
        var existingFile = new File(fileName, fileData);

        var result = this.fileRepository.save(fileName, fileData);
        assertTrue(result);
        assertTrue(this.fileStore.contains(existingFile));

        result = this.fileRepository.save(fileName, fileData);
        assertFalse(result);
        assertTrue(this.fileStore.contains(existingFile));
    }

    @Test
    void get_all_should_succeed_when_full() {
        var fileName1 = "file-1.txt";
        var fileData1 = "This is file 1 content".getBytes();
        var file1 = new File(fileName1, fileData1);
        var fileName2 = "file-2.txt";
        var fileData2 = "This is file 2 content".getBytes();
        var file2 = new File(fileName2, fileData2);
        var addedFiles = List.of(file1, file2);
        this.fileStore.addAll(addedFiles);

        var result = this.fileRepository.getAll();

        assertTrue(result.containsAll(addedFiles));
    }

    @Test
    void get_all_should_succeed_when_empty() {
        var result = this.fileRepository.getAll();

        assertTrue(result.isEmpty());
    }

    @Test
    void get_latest_should_return_expected_result() {
        var fileName1 = "file-1.txt";
        var fileData1 = "This is file 1 content".getBytes();
        var file1 = new File(fileName1, fileData1);
        var fileName2 = "file-2.txt";
        var fileData2 = "This is file 2 content".getBytes();
        var file2 = new File(fileName2, fileData2);
        var addedFiles = List.of(file1, file2);
        this.fileStore.addAll(addedFiles);

        var result = this.fileRepository.getLatest();

        assertTrue(result.isPresent());
        assertEquals(file2, result.get());
    }

    @Test
    void get_latest_should_succeed_when_empty() {
        var result = this.fileRepository.getLatest();

        assertTrue(result.isEmpty());
    }

    @Test
    void get_random_should_return_expected_result() {
        var fileName1 = "file-1.txt";
        var fileData1 = "This is file 1 content".getBytes();
        var file1 = new File(fileName1, fileData1);
        var fileName2 = "file-2.txt";
        var fileData2 = "This is file 2 content".getBytes();
        var file2 = new File(fileName2, fileData2);
        var addedFiles = List.of(file1, file2);
        this.fileStore.addAll(addedFiles);

        var result = this.fileRepository.getRandom();

        assertTrue(result.isPresent());
    }

    @Test
    void get_random_should_succeed_when_empty() {
        var result = this.fileRepository.getRandom();

        assertTrue(result.isEmpty());
    }

}
