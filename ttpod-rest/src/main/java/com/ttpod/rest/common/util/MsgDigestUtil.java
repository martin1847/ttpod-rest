package com.ttpod.rest.common.util;

import groovy.transform.CompileStatic;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 枚举一些消息摘要算法，简化摘要代码
 *
 * @author yangyang.cong
 */
@CompileStatic
public enum MsgDigestUtil {
    MD2("MD2"),
    MD5("MD5"),
    SHA("SHA-1"),
    SHA256("SHA-256"),
    SHA384("SHA-384"),
    SHA512("SHA-512");

//    MessageDigest msgDigest;
//    Lock lock = new ReentrantLock();
    private static final char[] DIGITS_LOWER;
    private static final char[] DIGITS_UPPER;

    final String name;

    private MsgDigestUtil(String name) {
        this.name = name;
//        try {
//            this.msgDigest = MessageDigest.getInstance(name);
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException("无法获取摘要算法:" + name, e);
//        }
    }

    // static case thread use diff INSTANCE error .
    final ThreadLocal<MessageDigest> mdLocal = new ThreadLocal<MessageDigest>();


    public byte[] digest(byte[] targets) {
        MessageDigest md = mdLocal.get();

        if(md == null){
            try {
                md = MessageDigest.getInstance(name);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("无法获取摘要算法:" + name, e);
            }
            mdLocal.set(md);
        }else {
            md.reset();
        }
        return md.digest(targets);

//        this.lock.lock();
//        try {
//            this.msgDigest.reset();
//            return this.msgDigest.digest(targets);
//        } finally {
//            this.lock.unlock();
//        }
    }

    public String digest2HEX(byte[] targets, boolean toLowerCase) {
        return new String(encodeHex(digest(targets), toLowerCase));
    }

    public String digest2HEX(String targets, boolean toLowerCase) {
        try {
            return digest2HEX(targets.getBytes("UTF-8"), toLowerCase);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public String digest2HEX(String targets) {
        return digest2HEX(targets, true);
    }

    public static final char[] encodeHex(byte[] data, boolean toLowerCase) {
        char[] toDigits = toLowerCase ? DIGITS_LOWER : DIGITS_UPPER;
        int l = data.length;
        char[] out = new char[l << 1];

        int i = 0;
        for (int j = 0; i < l; i++) {
            out[(j++)] = toDigits[((0xF0 & data[i]) >>> 4)];
            out[(j++)] = toDigits[(0xF & data[i])];
        }
        return out;
    }

    static {
        DIGITS_LOWER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        DIGITS_UPPER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    }
}