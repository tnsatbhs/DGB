package dgb;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Student already exists")
public class StudentExistsException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public StudentExistsException(String exception)
	{
		super(exception);
	}

}

