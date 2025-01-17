package pe.entelgy.backend.evfinal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.entelgy.backend.evfinal.model.User;
import pe.entelgy.backend.evfinal.repository.UserRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> getUser(String username){
        return userRepository.findByUsername(username);
    }
}
