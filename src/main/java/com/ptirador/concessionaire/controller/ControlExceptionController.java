package com.ptirador.concessionaire.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * Exception controller.
 *
 * @author ptirador
 */
@ControllerAdvice
public class ControlExceptionController {

    /**
     * Generic error page.
     */
    private static final String GENERIC_ERROR = "error";

    /**
     * Access denied error page.
     */
    private static final String ACCESS_DENIED_ERROR = "accessDenied";

    /**
     * Page not found error.
     */
    private static final String NOT_FOUND_ERROR = "notFound";

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ControlExceptionController.class);

    @ExceptionHandler({Exception.class, RuntimeException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public String defaultErrorHandler(HttpServletRequest request, Exception e) {
        LOG.error("URL has provoked an error: " + request.getRequestURL(), e);
        return GENERIC_ERROR;
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDenied(HttpServletRequest request, AccessDeniedException e) {
        LOG.error("Access denied for URL: " + request.getRequestURL(), e);
        return ACCESS_DENIED_ERROR;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUrlNotFound(NoHandlerFoundException e) {
        LOG.error("URL does not exist", e);
        return NOT_FOUND_ERROR;
    }
}
