package dgb;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Gradebook already exists")
public class GradebookExistsException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public GradebookExistsException(String exception)
	{
		super(exception);
	}

}

