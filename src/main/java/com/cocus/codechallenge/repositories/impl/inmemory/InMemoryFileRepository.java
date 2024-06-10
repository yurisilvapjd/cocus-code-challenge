package com.cocus.codechallenge.repositories.impl.inmemory;

import com.cocus.codechallenge.models.File;
import com.cocus.codechallenge.repositories.FileRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Lazy
@Repository("inMemoryFileRepository")
public class InMemoryFileRepository implements FileRepository {

    private final LinkedList<File> fileStore;

    public InMemoryFileRepository(LinkedList<File> fileStore) {
        this.fileStore = fileStore;
    }

    @Override
    public boolean save(String fileName, byte[] fileData) {
        var newFile = new File(fileName, fileData);
        return !this.fileStore.contains(newFile) && this.fileStore.add(newFile);
    }

    @Override
    public List<File> getAll() {
        return this.fileStore;
    }

    @Override
    public Optional<File> getLatest() {
        if (this.fileStore.isEmpty()) return Optional.empty();
        return Optional.ofNullable(this.fileStore.getLast());
    }

    @Override
    public Optional<File> getRandom() {
        if (this.fileStore.isEmpty()) return Optional.empty();
        return Optional.ofNullable(this.fileStore.get(new Random().nextInt(this.fileStore.size())));
    }
}
