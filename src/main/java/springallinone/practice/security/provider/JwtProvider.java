package springallinone.practice.security.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import springallinone.practice.security.customuser.CustomUserDetails;
import springallinone.practice.security.properties.JwtProperties;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

    private final SecretKey secretKey;
    private final JwtProperties properties;
//    private final UserDetailsService userDetailsService;

    public JwtProvider(JwtProperties properties
//            , UserDetailsService userDetailsService
    ) {
        this.properties = properties;
        this.secretKey = Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8));
//        this.userDetailsService = userDetailsService;
    }

    public String createToken(Long id, String email, String role) {
        Date now = new Date();
        return Jwts.builder()
                .subject(String.valueOf(id))
                .claim("email", email)
                .claim("role", role)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + properties.getExpiration()))
                .signWith(secretKey)
                .compact();
    }

//    public String createToken(String username) {
//        Date now = new Date();
//        return Jwts.builder()
//                .subject(username)
//                .issuedAt(now)
//                .expiration(new Date(now.getTime() + properties.getExpiration()))
//                .signWith(secretKey)
//                .compact();
//    }

    public Authentication getAuthentication(String token) {
        Claims claims = extractClaims(token);
        Long id = Long.parseLong(claims.getSubject());
        String email = claims.get("email", String.class);
        String role = claims.get("role", String.class);

//        String username = extractUsername(token);
//        UserDetails userDetails = userDetailsService.loadByUsername(username);

        CustomUserDetails userDetails = new CustomUserDetails(id, email, null, role);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

//    private String extractUsername(String token) {
//        Claims claims = Jwts.parser()
//                .verifyWith(secretKey)
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();
//        return claims.getSubject();
//    }
}
