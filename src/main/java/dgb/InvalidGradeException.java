package dgb;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid grade")
public class InvalidGradeException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public InvalidGradeException(String exception)
	{
		super(exception);
	}

}