package no.uio.smol.auth.no.uio.auth.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import java.util.UUID

@Entity
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Use uuid instead of long
    private val uuid: UUID? = null

//    // Generate a random UUID
//    val myUuid = UUID.randomUUID()
//    val myUuidAsString = myUuid.toString()
//
//    // Print the UUID
//    println("Generated UUID: $myUuid")

    private val username: String? = null
    private val email: String? = null
    private val password: String? = null
}