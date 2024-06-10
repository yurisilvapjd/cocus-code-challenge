package com.cocus.codechallenge.repositories;


import com.cocus.codechallenge.models.File;

import java.util.List;
import java.util.Optional;

public interface FileRepository {

    boolean save(String fileName, byte[] fileData);

    List<File> getAll();

    Optional<File> getLatest();

    Optional<File> getRandom();
}
