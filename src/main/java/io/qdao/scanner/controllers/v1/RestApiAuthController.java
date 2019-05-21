package io.qdao.scanner.controllers.v1;

import io.qdao.scanner.dto.AppUserDetails;
import io.qdao.scanner.dto.LoginRequestDto;
import io.qdao.scanner.dto.LoginResponseDto;
import io.qdao.scanner.exceptions.AuthUserIncorrectPasswordException;
import io.qdao.scanner.exceptions.AuthUserNotFoundException;
import io.qdao.scanner.exceptions.AuthenticationIsExistException;
import io.qdao.scanner.exceptions.UnknownTypeException;
import io.qdao.scanner.services.UserAuthenticateFactory;
import io.qdao.scanner.utils.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class RestApiAuthController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder encoder;

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto dto) {
        final Authentication authentication = SecurityUtil.getCurrentAuthentication();
        if (authentication != null) {
            throw new AuthenticationIsExistException();
        }

        final UserDetails details = userDetailsService.loadUserByUsername(dto.getUsername());
        if (details == null) {
            throw new AuthUserNotFoundException();
        }

        if (!encoder.matches(dto.getPassword(), details.getPassword())) {
            throw new AuthUserIncorrectPasswordException();
        }

        if (details instanceof AppUserDetails) {
            final AppUserDetails appUserDetails = (AppUserDetails) details;
            return new LoginResponseDto(UserAuthenticateFactory.TOKEN_PREFIX + appUserDetails.getToken());
        }

        throw new UnknownTypeException();
    }
}
