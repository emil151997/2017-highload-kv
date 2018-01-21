package ru.mail.polis.minosyan;

import org.jetbrains.annotations.NotNull;
import gnu.trove.map.hash.THashMap;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

public class MyFileDAO implements MyDAO {
    @NotNull
    private final File dir;
    private final THashMap<String,byte[]> Cache;
    public MyFileDAO(@NotNull File dir) {
        this.dir = dir;
        Cache = new THashMap<>(20000);
    }

    private File getFile(@NotNull final String key) {
        return new File(dir, key);
    }

    @NotNull
    @Override
    public byte[] get(@NotNull final String key) throws NoSuchElementException, IllegalArgumentException, IOException {
        keyCheck(key);
        byte[] value = Cache.get(key);
        if (value!=null){
            return value;
        }
        Path path = Paths.get(dir.getPath(), key);
        if (Files.notExists(path)) {
            throw new NoSuchElementException();
        }
        value = Files.readAllBytes(path);
        Cache.put(key,value);
        return Cache.get(key);
    }

    @Override
    public void upsert(@NotNull final String key, @NotNull final byte[] value) throws IllegalArgumentException, IOException {
        try (OutputStream os = new FileOutputStream(getFile(key))) {
            os.write(value);
        }
        Cache.remove(key);
    }

    @Override
    public void delete(@NotNull final String key) throws IllegalArgumentException, IOException {
        getFile(key).delete();
        Cache.remove(key);
    }

    private void keyCheck(String key) {
        if (key.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
