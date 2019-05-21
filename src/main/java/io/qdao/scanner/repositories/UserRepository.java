package io.qdao.scanner.repositories;

import io.qdao.scanner.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailContainingIgnoreCase(String email);
    User findByEmailAndUid(String email, Long uid);

    boolean existsByEmail(String email);
}
