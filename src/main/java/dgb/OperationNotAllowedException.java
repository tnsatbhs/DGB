package dgb;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Operation not allowed")
public class OperationNotAllowedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OperationNotAllowedException(String exception)
    {
        super(exception);
    }

}





