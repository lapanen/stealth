package org.lapanen.stealth.si.process.support;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.common.base.Preconditions;

public class InputStreamReader extends Thread {

    private final InputStream in;
    private byte[] result;

    public InputStreamReader(final InputStream stream) {
        in = Preconditions.checkNotNull(stream, "Input stream must not be null");
    }

    @Override
    public void run() {
        int bite;
        try (final ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            while ((bite = in.read()) != -1) {
                out.write(bite);
            }
            in.close();
            result = out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("While trying to read input stream", e);
        }
    }

    /**
     * @param waitForMillis
     *         used in calling {@link Thread#join(long)}
     *
     * @return
     *
     * @throws InterruptedException
     * @see
     */
    public byte[] getResult(final long waitForMillis) throws InterruptedException {
        join(waitForMillis);
        return result;
    }

}
