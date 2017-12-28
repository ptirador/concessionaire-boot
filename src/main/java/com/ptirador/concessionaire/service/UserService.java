package com.ptirador.concessionaire.service;

import com.ptirador.concessionaire.model.User;

import java.util.List;

/**
 * @author ptirador
 */
public interface UserService {

    /**
     * Finds a user bean by its username.
     *
     * @param username Username.
     * @return User bean object that matches the username. Null otherwise.
     */
    User findByUsername(String username);

    /**
     * Finds a user bean by its id.
     *
     * @param id Id.
     * @return User bean object that matches the id. Null otherwise.
     */
    User findById(String id);

    /**
     * Finds a user bean by its email.
     *
     * @param email email.
     * @return User bean object that matches the email. Null otherwise.
     */
    User findByEmail(String email);

    /**
     * Inserts a user bean into database.
     *
     * @param user User bean.
     * @return Number of affected registers.
     */
    int insertUser(User user);

    /**
     * Obtains all users.
     *
     * @return list of users.
     */
    List<User> findAll();

    /**
     * Saves a user object.
     *
     * @param user user object.
     * @return the saved object.
     */
    User save(User user);
}
