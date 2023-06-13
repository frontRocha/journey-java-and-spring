package br.com.book.repository.user;
import br.com.book.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer> {
    public Optional<User> findByLogin(String login);

    boolean existsByLogin(String login);
}
