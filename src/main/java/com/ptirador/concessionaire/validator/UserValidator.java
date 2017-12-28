package com.ptirador.concessionaire.validator;

import com.ptirador.concessionaire.model.User;
import com.ptirador.concessionaire.service.UserService;
import com.ptirador.concessionaire.util.Constants;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Login/Sign Up form validator for users.
 *
 * @author ptirador
 */
@Component
public class UserValidator implements Validator {

    /**
     * Form field names.
     */
    private static final String USERNAME_FIELD = "username";
    private static final String EMAIL_FIELD = "email";
    private static final String PSWD_FIELD = "password";

    /**
     * Error message codes.
     */
    private static final String MSG_MANDATORY_FIELD = "msg.mandatory.field";
    private static final String MSG_USERNAME_EXISTS = "Exists.userBean.username";
    private static final String MSG_EMAIL_EXISTS = "Exists.userBean.email";
    private static final String MSG_PSWD_INVALID = "Pattern.userBean.password";
    private static final String MSG_EMAIL_INVALID = "Pattern.userBean.email";

    /**
     * Autowired fields.
     */
    private final UserService userService;

    /**
     * Constructor.
     *
     * @param userService user service.
     */
    public UserValidator(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        User user = (User) obj;

        // Checking empty fields.
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, USERNAME_FIELD, MSG_MANDATORY_FIELD);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, EMAIL_FIELD, MSG_MANDATORY_FIELD);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, PSWD_FIELD, MSG_MANDATORY_FIELD);

        // Check if username already exists.
        User dbUser = userService.findByUsername(user.getUsername());
        if (null != dbUser) {
            errors.rejectValue(USERNAME_FIELD, MSG_USERNAME_EXISTS);
        }

        // Checking email format.
        if (StringUtils.hasLength(user.getEmail())) {
            Pattern pattern = Pattern.compile(Constants.EMAIL_PATTERN);
            Matcher matcher = pattern.matcher(user.getEmail());
            if (!matcher.matches()) {
                errors.rejectValue(EMAIL_FIELD, MSG_EMAIL_INVALID);
            } else {
                // Check if email already exists.
                dbUser = userService.findByEmail(user.getEmail());
                if (null != dbUser) {
                    errors.rejectValue(EMAIL_FIELD, MSG_EMAIL_EXISTS);
                }
            }
        }

        // Checking password policy.
        if (StringUtils.hasLength(user.getPassword())) {
            /* Password policy:
             * - At least 8 characters in length
             * - At least 1 number.
             * - At least 1 upper case letter.
             * - At least 1 lower case letter.
             * - At least 1 special character.
             */
            Pattern pattern = Pattern.compile(Constants.PSWD_PATTERN);
            Matcher matcher = pattern.matcher(user.getPassword());
            if (!matcher.matches()) {
                errors.rejectValue(PSWD_FIELD, MSG_PSWD_INVALID);
            }
        }

    }
}
