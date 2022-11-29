package spring.boot.yj.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.boot.yj.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmailIgnoreCase(String email);
	Optional<User> findByUsernameIgnoreCase(String username);
}
