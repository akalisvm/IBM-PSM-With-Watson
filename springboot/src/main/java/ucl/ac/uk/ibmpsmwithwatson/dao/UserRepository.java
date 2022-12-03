package ucl.ac.uk.ibmpsmwithwatson.dao;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;
import ucl.ac.uk.ibmpsmwithwatson.entity.User;

@Repository
public interface UserRepository extends Neo4jRepository<User, String> {

    @Query("match (n:User{email:$email}) return n")
    User getUserByEmail(String email);

}
