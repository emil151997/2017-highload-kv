package ru.mail.polis.minosyan;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

public class MyFileDAO implements MyDAO {
    @NotNull
    private final File dir;

    public MyFileDAO(@NotNull File dir) {
        this.dir = dir;
    }

    private File getFile(@NotNull final String key) {
        return new File(dir, key);
    }

    @NotNull
    @Override
    public byte[] get(@NotNull final String key) throws NoSuchElementException, IllegalArgumentException, IOException {
        keyCheck(key);
        Path path = Paths.get(dir.getPath(), key);
        if (Files.notExists(path)) {
            throw new NoSuchElementException();
        }
        return Files.readAllBytes(path);
    }

    @Override
    public void upsert(@NotNull final String key, @NotNull final byte[] value) throws IllegalArgumentException, IOException {
        try (OutputStream os = new FileOutputStream(getFile(key))) {
            os.write(value);
        }
    }

    @Override
    public void delete(@NotNull final String key) throws IllegalArgumentException, IOException {
        getFile(key).delete();
    }

    private void keyCheck(String key) {
        if (key.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
