package org.lapanen.stealth.si.process.support;

import com.google.common.base.Preconditions;

public class InputStreamReaderHolder {

    private final InputStreamReader[] readers;

    private long waitForMillis = 1000;

    public InputStreamReaderHolder(final InputStreamReader... readers) {
        this.readers = Preconditions.checkNotNull(readers);
    }

    /**
     * Calls {@link InputStreamReader#start()} on all readers in the order they are.
     */
    public void start() {
        for (final InputStreamReader reader : readers) {
            reader.start();
        }
    }

    public byte[] getResultOrEmptyByteArray(final int indexOfReader) throws InterruptedException {
        return getResult(indexOfReader, new byte[] { });
    }

    /**
     * @param indexOfReader
     * @param defaultReturnValue
     *         to be returned, if reader is not found with index {@code indexOfReader}.
     *
     * @return
     *
     * @throws InterruptedException
     */
    public byte[] getResult(final int indexOfReader, final byte[] defaultReturnValue) throws InterruptedException {
        Preconditions.checkArgument(indexOfReader >= 0, "Index must be zero or greater");
        if (indexOfReader >= readers.length) {
            return defaultReturnValue;
        }
        return readers[indexOfReader].getResult(waitForMillis);
    }

}
