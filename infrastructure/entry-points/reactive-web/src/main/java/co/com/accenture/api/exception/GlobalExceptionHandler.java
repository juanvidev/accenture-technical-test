package co.com.accenture.api.exception;

import co.com.accenture.model.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.webflux.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        if(ex instanceof BusinessException businessException){

            BussinessResponseException responseError = new BussinessResponseException(
                    businessException.getSuccess(),
                    businessException.getMessage(),
                    LocalDateTime.now(),
                    businessException.getErrorCode()
            );

            exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(businessException.getStatusCode()));
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

            return exchange.getResponse().writeWith(
                    Mono.fromCallable(() -> objectMapper.writeValueAsBytes(responseError))
                            .map(bytes -> exchange.getResponse().bufferFactory().wrap(bytes))
            );
        }

        if(ex instanceof ConstraintViolationException constraintViolationEx){
            CustomResponseException responseError = new CustomResponseException(
                    constraintViolationEx.getSuccess(),
                    constraintViolationEx.getMessage(),
                    constraintViolationEx.getTimestamp(),
                    constraintViolationEx.getErrorCode(),
                    constraintViolationEx.getErrors()
            );

            exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(constraintViolationEx.getStatusCode()));
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

            return exchange.getResponse().writeWith(
                    Mono.fromCallable(() -> objectMapper.writeValueAsBytes(responseError))
                            .map(bytes -> exchange.getResponse().bufferFactory().wrap(bytes))
            );
        }

        // Fallback para errores inesperados
        exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(500));
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        return exchange.getResponse().writeWith(
                Mono.fromCallable(() -> objectMapper.writeValueAsBytes(new UnexpectedErrorException(ex)))
                        .map(bytes -> exchange.getResponse().bufferFactory().wrap(bytes))
        );


    }
}
