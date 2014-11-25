package org.lapanen.stealth.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TemporaryFileOutputStreamClosable implements AutoCloseable {

    private File file;
    private FileOutputStream out;

    private final String prefix;
    private final String suffix;

    public TemporaryFileOutputStreamClosable(final String prefix, final String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public TemporaryFileOutputStreamClosable(final String prefix) {
        this(prefix, null);
    }

    public FileOutputStream getOutputStream() throws IOException {
        if (out == null) {
            out = new FileOutputStream(getFile());
        }
        return out;
    }

    public File getFile() throws IOException {
        if (file == null) {
            file = File.createTempFile(prefix, suffix);
        }
        return file;
    }

    @Override
    public void close() throws IOException {
        out.close();
        file.delete();
    }

}
