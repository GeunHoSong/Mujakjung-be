// JwtProvider.java
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtProvider {

    // 이 키는 Base64로 인코딩된 256비트 이상의 문자열이어야 합니다.
    // 안전하게 생성한 문자열을 여기에 넣으세요.
    private final String SECRET_KEY_STRING = "여기에_매우_긴_랜덤_Base64_문자열을_넣으세요_최소_32글자_이상";

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY_STRING);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(getSigningKey())
                .compact();
    }
}