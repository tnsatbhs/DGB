import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Gradebook {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Integer id;
	
	private String name;
	private ArrayList<Student> students;
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public ArrayList<Student> getAllStudents()
	{
		return this.students;
	}

}
