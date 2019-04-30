package dgb;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

@Repository
public interface GradebookRepository extends JpaRepository<Gradebook, Integer> {

    @Query("SELECT g FROM Gradebook g WHERE g.name = :name")
    Gradebook findByName(@Param("name") String name);
}
