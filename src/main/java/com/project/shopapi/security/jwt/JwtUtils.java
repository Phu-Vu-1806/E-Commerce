package com.project.shopapi.security.jwt;
import com.project.shopapi.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationMs;

    @Value("${app.jwtExpirationRf}")
    private int jwtExpirationRf;

    public String generateJwtTokenFromUsername(UserDetailsImpl userDetails){
        return generateJwtToken(userDetails.getUsername());
    }

    public String generateJwtToken(String username) {
//        lấy ra thông tin của người dùng đã được xác thực từ token, bắt buộc giá trị ở đây là UserDetailsImpl không thể User

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)  // type:Bearer
                .compact();
    }

//    lấy người dùng từ thông báo token
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());  // khóa không hợp lệ
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());      // token không hợp lệ
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());   // token hết hạn
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());   // không được hỗ trợ
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage()); // chuỗi yêu cầu JWT rỗng
        }

        return false;
    }
}

