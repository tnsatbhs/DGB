package dgb;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Secondary server sync failed")
public class SecondaryServerSyncFailedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SecondaryServerSyncFailedException(String exception)
    {
        super(exception);
    }

}