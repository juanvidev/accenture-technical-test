package co.com.accenture.api.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BussinessResponseException {
    private Boolean success = false;
    private String message;
    private LocalDateTime timestamp;
    private String errorCode;
}
