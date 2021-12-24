package cinema.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class AlreadyPurchasedException extends RuntimeException{
    public AlreadyPurchasedException(String message) {
        super(message);
    }
}
