package app.tools.auth.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey


@Component
class JwtService {
    var key: SecretKey = Jwts.SIG.HS256.key().build();

    private fun extractUsername(token: String?): String {
        return extractClaim(token) { it.subject }
    }

    private fun extractExpiration(token: String?): Date {
        return extractClaim(token) { it.expiration }
    }

    private fun <T> extractClaim(token: String?, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    private fun extractAllClaims(token: String?): Claims {
        return Jwts
            .parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload()
    }

    private fun isTokenExpired(token: String?): Boolean {
        return extractExpiration(token).before(Date())
    }

    fun validateToken(token: String?, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return (username == userDetails.username && !isTokenExpired(token))
    }


    fun GenerateToken(username: String): String {
        val claims: Map<String, Any?> = HashMap()
        return createToken(claims, username)
    }


    private fun createToken(claims: Map<String, Any?>, username: String): String {
        return Jwts.builder()
            .claims(claims)
            .subject(username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + 1000 * 60 * 1))
            .signWith(key)
            .compact()
    }
}