package org.lapanen.stealth.si.process.support;

import java.util.Map;

import org.lapanen.stealth.si.process.core.ProcessExecutor;
import org.lapanen.stealth.si.process.core.ProcessExecutorImpl;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.integration.expression.ExpressionUtils;
import org.springframework.messaging.Message;

public class ProcessExecutorIntegrationUtils {

    // TODO: remove these environment related members in favour of a HeaderMapper
    public static final String HEADER_NAME_ENVIRONMENT = "process_executor_environment";
    public static final String HEADER_NAME_ENVIRONMENT_PREFIX = HEADER_NAME_ENVIRONMENT + ".";
    public static final String createEnvironmentHeaderName(final String name) {
        return HEADER_NAME_ENVIRONMENT_PREFIX + name;
    }

    private static EvaluationContext STANDARD_CONTEXT;

    private ProcessExecutorIntegrationUtils() {
        // intentionally empty
    }

    public static ProcessExecutor prepareExecutor(final Message<?> message) {
        final ProcessExecutorImpl executor = new ProcessExecutorImpl(extractCommand(message));
        for (final String headerName : message.getHeaders().keySet()) {
            if (headerName.startsWith(HEADER_NAME_ENVIRONMENT_PREFIX)) {
                final String environmentName = headerName.substring(HEADER_NAME_ENVIRONMENT_PREFIX.length());
                executor.setEnvironmentVariable(environmentName, message.getHeaders().get(headerName, String.class));
            }
            if (headerName.equals(HEADER_NAME_ENVIRONMENT)) {
                executor.setEnvironmentVariables(message.getHeaders().get(headerName, Map.class));
            }
        }
        return executor;
    }

    public static String[] extractCommand(final Message<?> message) {
        final Object payload = message.getPayload();
        if (payload instanceof String) {
            final String expression = ((String) payload).trim();
            SpelExpressionParser parser = new SpelExpressionParser();
            return translateExpression(parser.parseExpression(expression, ParserContext.TEMPLATE_EXPRESSION), message);
        } else if (payload instanceof Expression) {
            return translateExpression((Expression) payload, message);
        }
        throw new IllegalArgumentException("Wrong type: " + payload.getClass().getCanonicalName());
    }

    private static EvaluationContext getStandardEvaluationContext() {
        if (STANDARD_CONTEXT == null) {
            STANDARD_CONTEXT = ExpressionUtils.createStandardEvaluationContext();
        }
        return STANDARD_CONTEXT;
    }

    private static String[] translateExpression(final Expression expression, final Message<?> message) {
        return expression.getValue(getStandardEvaluationContext(), message, String.class).split("\\s");
    }

}
