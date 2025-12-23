package co.com.accenture.model.exception;

public class BusinessException extends CustomBaseException {

    public BusinessException(String businessError, String message, int statusCode) {
        super(statusCode, message, businessError);
    }

    public BusinessException(String businessError, String message) {
        super(400, message, businessError);
    }
}
