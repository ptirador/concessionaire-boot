package com.ptirador.concessionaire.repository;

import com.ptirador.concessionaire.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Finds a user bean by its username.
     *
     * @param username Username.
     * @return User bean object that matches the username. Null otherwise.
     */
    User findByUsername(String username);

    /**
     * Finds a user bean by its email.
     *
     * @param email email.
     * @return User bean object that matches the email. Null otherwise.
     */
    User findByEmail(String email);
}
