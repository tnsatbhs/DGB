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

	private String name, secondaryHost;
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
	public List<Student> getStudents()
	{
		return this.students;
	}

	public void addStudent(Student student)
	{
		students.add(student);
	}
	public void removeStudent(String name) {
		for (int i = 0; i < this.students.size(); ++i) {
			if (this.students.get(i).getName().equals(name)) {
				this.students.remove(i);
				break;
			}
		}
	}
	public Student getStudent(String name) {
		for (Student s : this.students) {
			if (s.getName().equals(name)) {
				return s;
			}
		}
		return null;
	}


	@JsonIgnore
	public String getSecondaryHost() {
		return secondaryHost;
	}
	public void setSecondaryHost(String value) {
		secondaryHost = value;
	}

}
