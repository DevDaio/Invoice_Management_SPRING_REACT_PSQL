package com.example.Invoice_Management_SPRING_REACT_PSQL.DB_Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Invoice_Management_SPRING_REACT_PSQL.Classes.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByMail(String mail);

	void deleteByMail(String mail);

}
