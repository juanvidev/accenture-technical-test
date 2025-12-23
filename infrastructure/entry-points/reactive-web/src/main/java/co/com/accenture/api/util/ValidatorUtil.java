package co.com.accenture.api.util;

import co.com.accenture.api.exception.ConstraintViolationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ValidatorUtil {
    private final Validator validator;

    public <T> Mono<T> validate(T validationObj) {
        return Mono.fromCallable(() -> validator.validate(validationObj))
            .flatMap(violations -> violations.isEmpty()
                    ? Mono.just(validationObj)
                    : Mono.error(new ConstraintViolationException(
                            violations.stream()
                                    .map(ConstraintViolation::getMessage)
                                        .toList()
                    ))
                );
    }
}