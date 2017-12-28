package com.ptirador.concessionaire.interceptor;

import com.ptirador.concessionaire.model.User;
import com.ptirador.concessionaire.service.UserService;
import com.ptirador.concessionaire.util.Constants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

/**
 * @author ptirador
 */
public class AccessControlInterceptor extends HandlerInterceptorAdapter {

    /**
     * Service that manages users.
     */
    private UserService userService;

    /**
     * Locale resolver.
     */
    private LocaleResolver localeResolver;

    /**
     * Members.
     */
    private boolean userExists;

    /**
     * Constructor.
     *
     * @param userService    Service that manages users.
     * @param localeResolver Locale resolver.
     */
    public AccessControlInterceptor(final UserService userService,
                                    final LocaleResolver localeResolver) {
        this.userService = userService;
        this.localeResolver = localeResolver;
    }

    /**
     * @param request  HTTP request.
     * @param response HTTP response.
     * @param handler  Handler object.
     */
    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) {

        validateUserAccess(request, response);

        return true;
    }

    /**
     * Validate the user access for every request.
     *
     * @param request  HTTP request.
     * @param response HTTP response.
     */
    private void validateUserAccess(final HttpServletRequest request,
                                    final HttpServletResponse response) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute(Constants.USER_LOGIN);
        // Checking user registered in the application.
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // If user access for first time...
        if (null == sessionUser && null != auth) {
            String username = auth.getName();
            User dbUser = userService.findByUsername(username);
            this.setUserExists(null != dbUser);
            if (isUserExists()) {

                sessionUser = dbUser;

                // Set locale for user
                Locale locale = Locale.getDefault();
                this.localeResolver.setLocale(request, response, locale);
                if (null != sessionUser) {
                    sessionUser.setLanguage(locale.getLanguage());

                    // Add user bean to session to not query again in DB.
                    session.setAttribute(Constants.USER_LOGIN, sessionUser);
                }
            }
        }

        if (!isUserExists()) {
            session.removeAttribute(Constants.USER_LOGIN);
        }
    }

    /**
     * Obtains the userExists member.
     *
     * @return true if user exists, false otherwise.
     */
    public boolean isUserExists() {
        return userExists;
    }

    /**
     * Establishes the userExists member.
     *
     * @param userExists true if user exists, false otherwise.
     */
    public void setUserExists(boolean userExists) {
        this.userExists = userExists;
    }
}
