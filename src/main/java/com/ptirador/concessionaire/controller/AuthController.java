package com.ptirador.concessionaire.controller;

import com.ptirador.concessionaire.model.User;
import com.ptirador.concessionaire.security.service.MyUserDetailsService;
import com.ptirador.concessionaire.service.UserService;
import com.ptirador.concessionaire.util.Constants;
import com.ptirador.concessionaire.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Collection;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

/**
 * App login/signup controller.
 *
 * @author ptirador
 */
@Controller
public class AuthController {

    /**
     * URL names.
     */
    private static final String URL_ROOT = "/";
    private static final String URL_LOGIN = "/login";
    private static final String URL_SIGN_UP = "/signup";
    private static final String URL_HOME = "/home";

    /**
     * Model attribute names.
     */
    private static final String MDL_USER = "user";

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    /**
     * Error messages.
     */
    private static final String MSG_SIGNUP_KO = "signUp.result.ko";
    private static final Object MSG_SIGNUP_OK = "signUp.result.ok";
    private static final String MSG_BAD_CREDENTIALS = "signIn.badCredentials.result.ko";
    private static final String MSG_CREDENTIALS_EXPIRED = "signIn.credentialsExpired.result.ko";

    /**
     * Autowired elements.
     */
    private final UserService userService;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;
    private final MyUserDetailsService userDetailsService;

    /**
     * Constructor.
     *
     * @param userService        user service.
     * @param userValidator      user validator.
     * @param passwordEncoder    encoder for password.
     * @param userDetailsService security user service.
     */
    public AuthController(@Qualifier("userServiceImpl") final UserService userService,
                          final UserValidator userValidator,
                          final PasswordEncoder passwordEncoder,
                          final MyUserDetailsService userDetailsService) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Mapping for root page.
     *
     * @return Redirection to login page.
     */
    @GetMapping(URL_ROOT)
    public String getRoot() {
        return Constants.REDIRECT + URL_LOGIN;
    }

    /**
     * Mapping for returning login page.
     *
     * @param error Error code for login.
     * @param model Model object.
     * @return Login page.
     */
    @GetMapping(URL_LOGIN)
    public String getLogin(@RequestParam(required = false) final String error,
                           final Model model) {
        String page = URL_LOGIN;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            model.addAttribute(MDL_USER, new User());
            checkErrorCode(error, model);
        } else {
            page = Constants.REDIRECT + URL_HOME;
        }

        return page;
    }

    /**
     * Error code checking from login page.
     *
     * @param errorCode Error code.
     * @param model     Form model.
     */
    private void checkErrorCode(final String errorCode, final Model model) {
        if (null != errorCode) {
            switch (errorCode) {
                case Constants.LOGIN_ERROR_BAD_CREDENTIALS:
                    model.addAttribute(Constants.MSG_ERROR, MSG_BAD_CREDENTIALS);
                    break;
                case Constants.LOGIN_ERROR_CREDENTIALS_EXPIRED:
                    model.addAttribute(Constants.MSG_ERROR, MSG_CREDENTIALS_EXPIRED);
                    break;
                default:
                    LOG.error("Invalid error code: {}", errorCode);
            }
        }
    }

    /**
     * Mapping for returning sign up page.
     *
     * @param model Model object.
     * @return Sign ip page.
     */
    @GetMapping(URL_SIGN_UP)
    public String getSignUp(final Model model) {
        String page = URL_SIGN_UP;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            model.addAttribute(MDL_USER, new User());
        } else {
            page = Constants.REDIRECT + URL_HOME;
        }

        return page;
    }

    /**
     * Action for sending data to sign up.
     *
     * @param user    user information.
     * @param errors  variable that contains errors (if they exist).
     * @param model   form model.
     * @param rda     redirect attributes.
     * @param session session object.
     * @return Depending on the result, sign up view or redirect to login page.
     */
    @PostMapping(URL_SIGN_UP)
    public String postSignUp(@ModelAttribute(MDL_USER) final User user,
                             final Errors errors,
                             final Model model,
                             final RedirectAttributes rda,
                             final HttpSession session) {
        String resultPage = URL_SIGN_UP;

        userValidator.validate(user, errors);

        if (errors.hasErrors()) {
            LOG.error("Error while registering a user: {}", user);
        } else {
            // Password encryption
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            boolean resultOk = userService.insertUser(user) > 0;
            if (resultOk) {
                // Auto-login after successful registration.
                authenticateUserAndSetSession(user, session);
                resultPage = Constants.REDIRECT + URL_LOGIN;
                rda.addFlashAttribute(Constants.MSG, MSG_SIGNUP_OK);
            } else {
                model.addAttribute(Constants.MSG_ERROR, MSG_SIGNUP_KO);
            }
        }

        return resultPage;
    }

    /**
     * Auto-login after successful registration.
     *
     * @param user    user.
     * @param session session object.
     */
    private void authenticateUserAndSetSession(User user, HttpSession session) {
        String username = user.getUsername();

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (null != userDetails) {
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            Authentication token = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                    authorities);
            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(token);
            SecurityContextHolder.setContext(sc);
            // Creates context for the session.
            session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
            // Set necessary details in session
            session.setAttribute(Constants.USER_LOGIN, user);
        }
    }
}
