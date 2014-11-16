package org.lapanen.stealth.util;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;

public class StreamWritingResponseExtractor implements ResponseExtractor<HttpStatus> {

    private final OutputStream out;

    private final boolean closeStreamAfterExtracting;

    public StreamWritingResponseExtractor(final OutputStream out) {
        this(out, false);
    }

    public StreamWritingResponseExtractor(final OutputStream out, final boolean closeStreamAfterExtracting) {
        this.out = out;
        this.closeStreamAfterExtracting = closeStreamAfterExtracting;
    }

    @Override
    public HttpStatus extractData(final ClientHttpResponse response) throws IOException {
        try {
            int bite;
            while ((bite = response.getBody().read()) != -1) {
                out.write(bite);
            }
        } finally {
            if (closeStreamAfterExtracting) {
                out.close();
            }
        }
        return response.getStatusCode();
    }
}
