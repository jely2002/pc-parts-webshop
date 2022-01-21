package com.jelleglebbeek.pcparts.auth;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Argon2PasswordEncoder implements PasswordEncoder {

    private static final int DEFAULT_PARALLELISM = 1;
    private static final int DEFAULT_MEMORY = 1 << 12;
    private static final int DEFAULT_ITERATIONS = 3;

    private final int parallelism;
    private final int memory;
    private final int iterations;

    private final Argon2 argon2;

    public Argon2PasswordEncoder(int parallelism, int memory, int iterations) {
        this.parallelism = parallelism;
        this.memory = memory;
        this.iterations = iterations;
        this.argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
    }

    public Argon2PasswordEncoder() {
        this(DEFAULT_PARALLELISM, DEFAULT_MEMORY, DEFAULT_ITERATIONS);
    }

    @Override
    public String encode(CharSequence rawPassword) {
        String hash = argon2.hash(iterations, memory, parallelism, rawPassword.toString());
        argon2.wipeArray(rawPassword.toString().toCharArray());
        return hash;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String hash) {
        boolean match = argon2.verify(hash, rawPassword.toString());
        argon2.wipeArray(rawPassword.toString().toCharArray());
        return match;
    }
}
