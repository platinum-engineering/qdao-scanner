package io.qdao.scanner.services;

import io.qdao.scanner.models.User;
import io.qdao.scanner.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
class JwtService {

    private static final Key SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;

    User parseToken(String token) {
        try {
            final Claims body = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();

            final String email = body.getSubject();
            final long createdAt = (long) body.get("created_at");
            final Long uid = (long) (int) body.get("uid");
            if (logger.isDebugEnabled()) {
                logger.debug("Token for ".concat(email).concat(" [").concat(String.valueOf(uid)).concat("] ")
                        .concat(" created at ").concat(new Date(createdAt).toString()));
            }
            return userRepository.findByEmailAndUid(email, uid);
        } catch (JwtException | ClassCastException e) {
            return null;
        }
    }

    String generateToken(User u) {
        final Claims claims = Jwts.claims()
                .setSubject(u.getEmail());
        claims.put("created_at", new Date().getTime());
        claims.put("uid", u.getUid());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SECRET)
                .compact();
    }
}
