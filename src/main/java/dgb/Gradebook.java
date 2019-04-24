package dgb;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Gradebook {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Integer id;

	private String name;
	private Boolean isPrimaryServer;
	private ArrayList<Student> students = new ArrayList<Student>();

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getId()
	{
		return this.id;
	}


	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return this.name;
	}

	public void setIsPrimaryServer(Boolean value) {
		this.isPrimaryServer = value;
	}
	@JsonIgnore
	public Boolean getIsPrimaryServer() {
		return this.isPrimaryServer;
	}



	@JsonIgnore
	public List<Student> getAllStudents()
	{
		return this.students;
	}

	public void addStudent(Student student)
	{
		students.add(student);
	}

}
