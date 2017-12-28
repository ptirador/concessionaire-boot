package com.ptirador.concessionaire.security.service;

import com.ptirador.concessionaire.model.User;
import com.ptirador.concessionaire.repository.UserRepository;
import com.ptirador.concessionaire.util.Constants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ptirador
 */
@Component
public class MyUserDetailsService implements UserDetailsService {

    /**
     * User repository.
     */
    private final UserRepository userRepository;

    /**
     * Constructor.
     *
     * @param userRepository User repository.
     */
    public MyUserDetailsService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Obtains a user from DB by its username.
     *
     * @param username User Name.
     * @return User details.
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        UserDetails userDetails;

        if (!StringUtils.isEmpty(username)) {
            User user = userRepository.findByUsername(username);
            if (null != user) {
                List<GrantedAuthority> authorities = getAuthorities(user.getRole());
                userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
            } else {
                String errorMsg = MessageFormat.format("Username {0} not found", username);
                throw new UsernameNotFoundException(errorMsg);
            }
        } else {
            String errorMsg = "Username is empty";
            throw new UsernameNotFoundException(errorMsg);
        }

        return userDetails;
    }

    /**
     * Obtains the authority corresponding to the role id from a user.
     *
     * @param roleId Role identifier.
     * @return List of user authorities.
     */
    private List<GrantedAuthority> getAuthorities(int roleId) {
        List<GrantedAuthority> authList = new ArrayList<>();
        switch (roleId) {
            case Constants.ROLE_ADMIN_ID:
                authList.add(new SimpleGrantedAuthority(Constants.ROLE_ADMIN));
                break;
            case Constants.ROLE_USER_ID:
                authList.add(new SimpleGrantedAuthority(Constants.ROLE_USER));
                break;
            default:
                break;
        }
        return authList;
    }
}