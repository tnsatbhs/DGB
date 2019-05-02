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
	protected Integer id;

	private String name, secondaryHost;
	private Boolean isPrimaryServer;
	private ArrayList<Student> students = new ArrayList<>();

	// Copy constructor.
	public Gradebook(Gradebook gradebook) {
		this.setId(gradebook.getId());
		this.setName(gradebook.getName());
		this.setSecondaryHost(gradebook.getSecondaryHost());
		this.setIsPrimaryServer(gradebook.getIsPrimaryServer());
		// Deep copy students.
		for (Student student : gradebook.students) {
			this.students.add(new Student(student));
		}
	}

	// Default constructor.
	public Gradebook() { }

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



	// Do not put @JsonIgnore here. When we need to return gradebooks without students, use AllGradebooks.
	public ArrayList<Student> getStudents() {
		return this.students;
	}

	public void setStudents(ArrayList<Student> students) {
		this.students = students;
	}

	public void addStudent(Student student) {
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
