package io.qdao.scanner.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidLoanModel extends RuntimeException {

    public InvalidLoanModel() {
        super("Invalid information about loan. Please check info in data base.");
    }
}
