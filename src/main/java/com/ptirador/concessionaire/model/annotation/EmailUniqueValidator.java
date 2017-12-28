package com.ptirador.concessionaire.model.annotation;

import com.ptirador.concessionaire.model.User;
import com.ptirador.concessionaire.repository.UserRepository;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Annotation to check if a user e-mail is already registered.
 *
 * @author ptirador
 */
public class EmailUniqueValidator implements ConstraintValidator<EmailUnique, User> {

    /**
     * User repository.
     */
    private UserRepository userRepository;

    /**
     * Constructor.
     *
     * @param userRepository user repository.
     */
    public EmailUniqueValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void initialize(EmailUnique constraint) {
        // No need to initialize.
    }

    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            User userDb = userRepository.findByEmail(user.getEmail());
            if (userDb != null && !user.getId().equals(userDb.getId())) {
                context.buildConstraintViolationWithTemplate("{Exists.userBean.email}").addPropertyNode("email")
                        .addConstraintViolation();
                return false;
            }
        }


        return true;
    }
}
