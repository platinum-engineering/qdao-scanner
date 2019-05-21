package io.qdao.scanner.filters;

import io.qdao.scanner.dto.AppUserDetails;
import io.qdao.scanner.registrars.RestAuthenticationEntryPoint;
import io.qdao.scanner.services.UserAuthenticateFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class JWTAuthenticationFilter extends BasicAuthenticationFilter {


    private UserAuthenticateFactory userAuthenticateFactory;

    public JWTAuthenticationFilter(UserAuthenticateFactory factory, AuthenticationManager manager, RestAuthenticationEntryPoint entryPoint) {
        super(manager, entryPoint);
        this.userAuthenticateFactory = factory;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        final String header = req.getHeader(UserAuthenticateFactory.HEADER_NAME);

        if (header == null || !header.startsWith(UserAuthenticateFactory.TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        final String authToken = header.substring(UserAuthenticateFactory.TOKEN_PREFIX.length());

        final UsernamePasswordAuthenticationToken authentication = getAuthentication(authToken);

        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            res.setHeader(UserAuthenticateFactory.HEADER_NAME, authToken);
        }

        chain.doFilter(req, res);

        if (authentication != null) {
            SecurityContextHolder.clearContext();
            haltCurrentSession(req);
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String authToken) {

        if (authToken == null || authToken.isEmpty()) {
            return null;
        }

        final AppUserDetails details = userAuthenticateFactory.makeDetailsFromToken(authToken);
        return userAuthenticateFactory.makeAuthentication(details);
    }

    private void haltCurrentSession(HttpServletRequest req) {
        final HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
