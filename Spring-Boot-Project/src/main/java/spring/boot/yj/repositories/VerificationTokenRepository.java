package spring.boot.yj.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.boot.yj.entities.User;
import spring.boot.yj.entities.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
	Optional<VerificationToken> findByToken(String token);
	void deleteByUser(User user);
}
