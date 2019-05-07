# DGB Java-project 
This project is an implementation of DGB (Distributed GradeBooks), a distributed server-side implementation of gradeBook.
## The feature of this distributed gradebook
* client requests creation of a GradeBook and supplies the GradeBook title
* system checks that the title is valid and does not already exist
* system creates the GradeBook and assigns a GradeBook id
* system returns the GradeBook id to the client
* client requests the list of all GradeBooks and system returns the list
* client requests creation of a secondary copy and supplies the GradeBook id
* the server that receives the request validates the GradeBook id
* the server sets up a secondary copy of the GradeBook
* client delete secondary copy and supplies the GradeBook id, the server removes its secondary copy of the GradeBook
* client requests deletion of a GradeBook and supplies the GradeBook id
* system validates the GradeBook id and removes all copies of the GradeBook
* client requests creation of a student and supplies the GradeBook id and student content
### Rest Api Endpoints
* /gradebook
* /gradebook/`<name>`
* /gradebook/`<id>`
* /secondary/`<id>`
* /gradebook/`<id>`/student
* /gradebook/`<id>`/student/`<name>`
* /gradebook/`<id>`/student/`<name>`/grade/`<grade>`
  
### Programming Language
* Java
### Development Tools
* SpringBoot Java Based Platform
* Spring Boot 2.1.4.RELEASE requires Java 8 and is compatible up to Java 11 (included). Spring Framework 5.1.6.RELEASE or        above is also required.
* Build Tool Maven, version 3.3+
* IDE IntelliJ IDEA or Eclipse 
* how to set up secondary port in intelliJ IDE
![Screen Shot 2019-05-06 at 9 55 49 PM](https://user-images.githubusercontent.com/44858342/57266663-f31a7900-704a-11e9-809f-2324cbaa0691.png)
