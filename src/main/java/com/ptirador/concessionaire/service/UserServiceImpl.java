package com.ptirador.concessionaire.service;

import com.ptirador.concessionaire.model.User;
import com.ptirador.concessionaire.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public User findById(final String id) {
        return userRepository.findOne(id);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public int insertUser(final User user) {
        return null != userRepository.save(user) ? 1 : 0;
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
