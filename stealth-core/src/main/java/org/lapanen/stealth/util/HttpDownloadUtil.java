package org.lapanen.stealth.util;

import java.io.OutputStream;
import java.net.URI;

import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestOperations;

public class HttpDownloadUtil {

    private HttpDownloadUtil() {
        // Call if you're God.
    }

    public static final void writeGetUrlTargetToStream(final URI uri, final OutputStream out, final RestOperations rest) {
        final StreamWritingResponseExtractor extractor = new StreamWritingResponseExtractor(out);
        rest.execute(uri, HttpMethod.GET, null, extractor);
    }

}
