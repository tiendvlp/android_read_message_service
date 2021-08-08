package com.devlogs.client_android.encrypt

import com.devlogs.client_android.BuildConfig
import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.nio.charset.StandardCharsets
import java.security.Key
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

class Encrypter {
    private val signatureAlgorithm: SignatureAlgorithm = SignatureAlgorithm.HS256

    @Inject
    constructor()
    fun createJWT(payload:String): String? {
        val signingKey: Key = SecretKeySpec(BuildConfig.SECRET_KEY.toByteArray(StandardCharsets.UTF_8), signatureAlgorithm.getJcaName())
        val builder: JwtBuilder = Jwts.builder()
            .setPayload(payload)
            .signWith(signatureAlgorithm ,signingKey)
        return builder.compact()
    }
}