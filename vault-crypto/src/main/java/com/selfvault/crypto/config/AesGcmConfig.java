package com.selfvault.crypto.config;

public class AesGcmConfig {
    private AesGcmConfig() {}

    public static final int SALT_LENGTH = 16;
    public static final int IV_LENGTH = 12;
    public static final int TAG_LENGTH_BIT = 128;
    public static final int ITERATIONS = 65536;

}
