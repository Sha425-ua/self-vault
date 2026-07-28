package com.selfvault.crypto.utils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class TypeConverter {
    public static byte[] charArrayToByteArray(char[] chars) {
        ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(CharBuffer.wrap(chars));
        byte[] bytes = Arrays.copyOf(byteBuffer.array(), byteBuffer.limit());
        Arrays.fill(byteBuffer.array(), (byte) 0);
        return bytes;
    }

    public static char[] byteArrayToCharArray(byte[] bytes) {
        CharBuffer charBuffer = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(bytes));
        char[] chars = Arrays.copyOf(charBuffer.array(), charBuffer.limit());
        Arrays.fill(charBuffer.array(), '\0');
        return chars;
    }
}
