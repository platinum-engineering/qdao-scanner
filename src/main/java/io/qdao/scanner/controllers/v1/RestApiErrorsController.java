package io.qdao.scanner.controllers.v1;

import io.qdao.scanner.exceptions.ApiInternalSeverException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

@RestController
@RestControllerAdvice(basePackages = {"io.qdao.scanner.controllers.v1"})
public class RestApiErrorsController {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Throwable> apiException(final Throwable throwable, HttpServletResponse response) {
        final HttpStatus responseStatus;
        if (throwable instanceof ApiInternalSeverException) {
            final ApiInternalSeverException e = (ApiInternalSeverException) throwable;
            responseStatus = e.getChildStatus();
        } else {
            final Class clazz = throwable.getClass();
            final ResponseStatus status = AnnotationUtils.findAnnotation(clazz, ResponseStatus.class);
            if (status != null) {
                responseStatus = status.value();
            } else {
                responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>(responseStatus);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Throwable> apiAccessDeniedException(AccessDeniedException e) {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
