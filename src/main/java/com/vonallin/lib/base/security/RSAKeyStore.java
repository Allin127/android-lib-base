package com.vonallin.lib.base.security;

import lombok.Builder;
import lombok.Data;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Builder
@Data
public class RSAKeyStore {
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;
}
