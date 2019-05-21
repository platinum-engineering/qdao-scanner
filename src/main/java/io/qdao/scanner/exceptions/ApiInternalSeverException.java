package io.qdao.scanner.exceptions;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ApiInternalSeverException extends AbstractRestApiException {

    private final Throwable child;

    public ApiInternalSeverException(Throwable throwable) {
        this.child = throwable;
    }

    public HttpStatus getChildStatus() {
        if (child != null) {
            final ResponseStatus status = AnnotationUtils.findAnnotation(child.getClass(), ResponseStatus.class);
            if (status != null) {
                return status.value();
            }
        }

        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @Override
    public String getMessage() {
        if (child == null) {
            return super.getMessage();
        }
        return child.getMessage();
    }
}
