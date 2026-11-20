package pe.upeu.beneficiarios.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pe.upeu.beneficiarios.entity.security.Usuario;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private final SecretKey clave;
    private final long expiracionMinutos;

    public JwtService(
            @Value("${app.jwt.secret:ILPEwcQMZU9TnjpXzv/hRWNoC9n1Imz+nPTBwMZH7TBX9n7f7XagfIgD7LhCEFZNclj1zxfx9FbVvbpfH45HOA==}") String secretoBase64,
            @Value("${app.jwt.expiracion-minutos:120}") long expiracionMinutos) {

        this.clave = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretoBase64));
        this.expiracionMinutos = expiracionMinutos;
    }

    public String generarToken(UserDetails userDetails, Usuario usuario) {
        Instant ahora = Instant.now();
        Instant expira = ahora.plus(expiracionMinutos, ChronoUnit.MINUTES);

        List<String> authorities = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        var builder = Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("authorities", authorities)
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(expira));

        if (usuario != null && usuario.getFacultad() != null) {
            builder.claim("facultadId", usuario.getFacultad().getId());
        }
        if (usuario != null && usuario.getProyecto() != null) {
            builder.claim("proyectoId", usuario.getProyecto().getId());
        }

        return builder.signWith(clave).compact();
    }

    public String extraerUsername(String token) {
        return leerClaims(token).getSubject();
    }

    public Claims leerClaims(String token) {
        return Jwts.parser()
                .verifyWith(clave)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public long getExpiracionSegundos() {
        return expiracionMinutos * 60;
    }
}
