package app.tools.auth.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey


@Service
class JwtService {
    private var key: SecretKey
    private val stringKey: String? = System.getenv("JWT_SECRET_KEY")

    init {
        key = if (stringKey.isNullOrEmpty()) {
            Jwts.SIG.HS256.key().build()
        } else {
            Keys.hmacShaKeyFor(stringKey.toByteArray())
        }
    }

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

    fun generateToken(username: String, role: String): String {
        val claims: Map<String, Any?> = mapOf("role" to role)
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