package com.ptirador.concessionaire.security.handler;

import com.ptirador.concessionaire.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;

/**
 * Failure handler in user authentication.
 *
 * @author ptirador
 */
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(MyAuthenticationFailureHandler.class);

    /**
     * Constants.
     */
    private static final String URL_LOGIN_ERROR = "/login?error=";

    /**
     * Object for redirection.
     */
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException e) throws IOException {
        String username = request.getParameter(SPRING_SECURITY_FORM_USERNAME_KEY);
        String errorCode;

        if (e instanceof CredentialsExpiredException) {
            LOG.error("Credentials expired for user: {}", username, e);
            errorCode = Constants.LOGIN_ERROR_CREDENTIALS_EXPIRED;

        } else {
            LOG.error("Bad credentials for user: {}", username, e);
            errorCode = Constants.LOGIN_ERROR_BAD_CREDENTIALS;
        }

        this.redirectStrategy.sendRedirect(request, response, URL_LOGIN_ERROR + errorCode);
    }
}