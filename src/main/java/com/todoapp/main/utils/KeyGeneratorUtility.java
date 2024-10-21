package com.todoapp.main.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class KeyGeneratorUtility {

    public static KeyPair generateRSAkey() {
        KeyPair keypair;
        try {
            KeyPairGenerator keypairgen = KeyPairGenerator.getInstance("RSA");
            keypairgen.initialize(2048);
            keypair = keypairgen.generateKeyPair();
        } catch (Exception e) {
            throw new IllegalStateException();
        }

        return keypair;
    }
}
