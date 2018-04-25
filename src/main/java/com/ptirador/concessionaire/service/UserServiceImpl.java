package com.ptirador.concessionaire.service;

import com.ptirador.concessionaire.model.User;
import com.ptirador.concessionaire.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author ptirador
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     * User repository.
     */
    private UserRepository userRepository;

    /**
     * Constructor.
     *
     * @param userRepository user repository.
     */
    public UserServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByUsername(final String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(final String id) {
        return userRepository.findById(id);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User insertUser(final User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
