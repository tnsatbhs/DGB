package dgb;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Secondary server can not edit resource")
public class SecondarySyncFailedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SecondarySyncFailedException(String exception) {
		super(exception);
	}

}
