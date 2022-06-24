package com.syeet.server.config.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author xhl
 * @date 2022/1/14
 */
public class Encoder {
    public static void main(String[] args) {
        String code = "123";
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(code);
        System.out.println(encode);
    }
}
