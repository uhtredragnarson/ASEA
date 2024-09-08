package com.epam.ASEAT.util.exceptions;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handle all RuntimeExceptions and return a generic message.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex, WebRequest request) {
        // Log the exception details for troubleshooting
        logger.error("RuntimeException occurred: {}", ex.getMessage());

        // Return a generic message to the user
        return new ResponseEntity<>("Something went wrong, please try again.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle Feign-specific exceptions and wrap them in a user-friendly message.
     */
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<String> handleFeignException(FeignException ex, WebRequest request) {
        // Log the exception details for troubleshooting
        logger.error("FeignException occurred: {}", ex.getMessage());

        // Return a more user-friendly message for Feign exceptions
        return new ResponseEntity<>("We are having trouble fetching data from one of our providers, please try again later.", HttpStatus.BAD_GATEWAY);
    }
}
