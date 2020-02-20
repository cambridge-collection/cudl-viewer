package ulcambridge.foundations.viewer;

import com.google.common.collect.ImmutableList;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ErrorHandlingControllerAdvice {
    /**
     * Handle Spring data binding errors thrown from controller methods with HTTP Bad Request responses describing the
     * validation error.
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse onDataBindError(
        BindException e) {
        List<Violation> violations = new ArrayList<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            violations.add(
                new Violation(fieldError.getField(), fieldError.getDefaultMessage()));
        }
        return new ValidationErrorResponse(violations);
    }

    public static class ValidationErrorResponse {
        private final List<Violation> violations;

        public ValidationErrorResponse(Iterable<Violation> violations) {
            this.violations = ImmutableList.copyOf(violations);
        }

        public List<Violation> getViolations() {
            return violations;
        }
    }

    public static class Violation {
        private final String fieldName;
        private final String message;

        public Violation(String fieldName, String message) {
            this.fieldName = fieldName;
            this.message = message;
        }

        public String getFieldName() {
            return fieldName;
        }

        public String getMessage() {
            return message;
        }
    }
}
