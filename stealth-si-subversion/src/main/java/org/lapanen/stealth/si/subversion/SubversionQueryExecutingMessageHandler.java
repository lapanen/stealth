package org.lapanen.stealth.si.subversion;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.handler.AbstractMessageHandler;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandlingException;
import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNLogClient;
import org.tmatesoft.svn.core.wc.SVNRevision;

public class SubversionQueryExecutingMessageHandler extends AbstractMessageHandler {

    static {
        DAVRepositoryFactory.setup();
    }

    private static final Logger log = LoggerFactory.getLogger(SubversionQueryExecutingMessageHandler.class);

    public static final String REPOSITORY_URL_HEADER_NAME = "svn_repository_url";

    private final Map<SVNURL, SVNLogClient> clients;

    private final MessageChannel outputChannel;

    public SubversionQueryExecutingMessageHandler(final Map<SVNURL, SVNLogClient> clients, final MessageChannel outputChannel) {
        this.outputChannel = outputChannel;
        this.clients = clients;
    }

    @Override
    protected void handleMessageInternal(final Message<?> message) throws Exception {
        final Object payload = message.getPayload();
        SVNRevision revision = null;
        if (payload instanceof String) {
            revision = SVNRevision.parse((String) payload);
        } else if (payload instanceof Long) {
            revision = SVNRevision.create((Long) payload);
        } else if (payload instanceof SVNRevision) {
            revision = (SVNRevision) payload;
        } else {
            throw new MessageHandlingException(message,
                    "Expected payload of type String, Long or " + SVNRevision.class.getCanonicalName() + ". Received [" + message.getPayload().getClass()
                            + "]");
        }

        SVNLogClient client = null;
        SVNURL repositoryUrl = null;
        final Object url = message.getHeaders().get(REPOSITORY_URL_HEADER_NAME);
        if (url == null) {
            if (clients.size() > 1) {
                throw new MessageHandlingException(message, "More than one client configured, header '" + REPOSITORY_URL_HEADER_NAME + "' must be present");
            }
            repositoryUrl = clients.keySet().iterator().next();
            client = clients.get(repositoryUrl);
        } else {
            if (url instanceof String) {
                repositoryUrl = SVNURL.parseURIEncoded((String) url);
            } else if (url instanceof SVNURL) {
                repositoryUrl = (SVNURL) url;
            }
        }

        log.debug("Getting log entry from repo {} for revision {}", repositoryUrl, revision);
        client.doLog(repositoryUrl, null, revision, revision, revision, false, true, 1, new ISVNLogEntryHandler() {

            @Override
            public void handleLogEntry(final SVNLogEntry logEntry) throws SVNException {
                outputChannel.send(MessageBuilder.withPayload(logEntry).build());
            }
        });
    }

}
