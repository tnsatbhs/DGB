package dgb;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.*;



class ShortGradebook {
	public String name;
	public Integer id;
}


@JsonRootName("Gradebooks")
public class AllGradebooks extends ArrayList<ShortGradebook> {

	// Copy constructor
	public AllGradebooks(Gradebooks gradebooks) {
		for (Gradebook gradebook : gradebooks) {
			ShortGradebook sg = new ShortGradebook();
			sg.name = gradebook.getName();
			sg.id = gradebook.getId();
			this.add(sg);
		}
	}
	
	public AllGradebooks() {
		
	}


}
