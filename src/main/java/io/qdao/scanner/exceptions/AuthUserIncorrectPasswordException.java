package io.qdao.scanner.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthUserIncorrectPasswordException extends AbstractRestApiException {
}
