package com.cell.bee.rate.gateway.sentinel.handler;

import com.cell.base.core.annotations.ActivePlugin;
import com.cell.gate.common.utils.GatewayUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-12 16:36
 */
@ActivePlugin
public class GatewayBlockHandler implements IGatewayBlockHandler
{
    private static final String DEFAULT_BLOCK_MSG_PREFIX = "Blocked by Sentinel: ";

    @Override
    public Mono<ServerResponse> handleRequest(ServerWebExchange exchange, Throwable ex)
    {
        if (acceptsHtml(exchange))
        {
            return htmlErrorResponse(ex);
        }
        // JSON result by default.
        return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(buildErrorResult(ex)));
    }

    private Mono<ServerResponse> htmlErrorResponse(Throwable ex)
    {
        return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                .contentType(MediaType.TEXT_PLAIN)
                .syncBody(DEFAULT_BLOCK_MSG_PREFIX + ex.getClass().getSimpleName());
    }

    private ErrorResult buildErrorResult(Throwable ex)
    {
        return new ErrorResult(HttpStatus.TOO_MANY_REQUESTS.value(),
                DEFAULT_BLOCK_MSG_PREFIX + ex.getClass().getSimpleName());
    }

    /**
     * Reference from {@code DefaultErrorWebExceptionHandler} of Spring Boot.
     */
    private boolean acceptsHtml(ServerWebExchange exchange)
    {
        try
        {
            List<MediaType> acceptedMediaTypes = exchange.getRequest().getHeaders().getAccept();
            acceptedMediaTypes.remove(MediaType.ALL);
            MediaType.sortBySpecificityAndQuality(acceptedMediaTypes);
            return acceptedMediaTypes.stream()
                    .anyMatch(MediaType.TEXT_HTML::isCompatibleWith);
        } catch (InvalidMediaTypeException ex)
        {
            return false;
        }
    }

    // FIXME : CUSTOMIZE
    @Override
    public Mono<Void> denyForReason(ServerWebExchange exchange)
    {
        return GatewayUtils.fastFinish(exchange, "block");
    }

    private static class ErrorResult
    {
        private final int code;
        private final String message;

        ErrorResult(int code, String message)
        {
            this.code = code;
            this.message = message;
        }

        public int getCode()
        {
            return code;
        }

        public String getMessage()
        {
            return message;
        }
    }
}
