package dgb;


import java.io.Serializable;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "student")
public class Student implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private String grade;

	public Student() { }

	// Copy constructor
	public Student(Student student) {
		this.setName(student.getName());
		this.setGrade(student.getGrade());
	}


	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getGrade()
	{
		return grade;
	}

	public void setGrade(String grade)
	{
		this.grade = grade;
	}

}
