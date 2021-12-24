package cinema.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class SeatOutOfBoundExceprion extends RuntimeException {
    public SeatOutOfBoundExceprion(String message) {
        super(message);
    }
}
