package co.com.accenture.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GenericResponse<T>{
    Boolean success;
    String message;
    LocalDateTime timestamp = LocalDateTime.now();
    T data;
}
