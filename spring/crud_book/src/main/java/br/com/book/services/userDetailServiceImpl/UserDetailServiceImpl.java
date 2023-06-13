package br.com.book.services.userDetailServiceImpl;

import br.com.book.businessException.BusinessException;
import br.com.book.data.UserDetail;
import br.com.book.models.user.User;
import br.com.book.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    public UserDetailServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = repository.findByLogin(username);

        verifyUserEmpty(user, username);

        return new UserDetail(user);
    }

    private void verifyUserEmpty(Optional<User> user, String username) throws UsernameNotFoundException {
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Usuário [" + username + "] não encontrado");
        }
    }

    public void existsByLogin(String login) throws BusinessException {
        validationBookName(login);
    }

    private void validationBookName(String login) throws BusinessException {
        if(repository.existsByLogin(login)) {
            throw new BusinessException("The user: " + login + " is already in use");
        }
    }
}
