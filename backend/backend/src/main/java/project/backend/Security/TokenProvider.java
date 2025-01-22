package project.backend.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Config.jwt.JwtProperties;
import project.backend.Entity.UserEntity;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class TokenProvider {

    @Autowired
    private JwtProperties jwtProperties;

    // JWT 토큰 생성
    public String createToken(UserEntity userEntity)
    {
        String secretKey = jwtProperties.getSecretKey();
        System.out.println("JWT Secret: " + secretKey);

        Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));
        // 기한 : 지금으로부터 1시간

        System.out.println("JWT Secret: " + jwtProperties.getSecretKey());

        // 생성
        return Jwts.builder().signWith(SignatureAlgorithm.HS512, jwtProperties.getSecretKey())
                .setSubject(userEntity.getUserid())
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .compact();
    }

    // 토큰 디코딩 + 위조 여부 확인 -> 아이디 리턴
    public String validateAndGetUserId(String token) {

        Claims claims = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

}
