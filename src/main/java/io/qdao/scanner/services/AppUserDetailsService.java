package io.qdao.scanner.services;

import io.qdao.scanner.models.User;
import io.qdao.scanner.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAuthenticateFactory userAuthenticateFactory;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userRepository.findByEmailContainingIgnoreCase(username);
        if (user == null) {
            logger.debug(String.format("User not found with email '%s'.", username));
            return null;
        }
        return userAuthenticateFactory.makeDetailsFromUser(user);
    }
}
