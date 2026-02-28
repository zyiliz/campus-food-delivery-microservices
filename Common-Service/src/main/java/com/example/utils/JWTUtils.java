package com.example.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/*
* JWT令牌的格式为：头部、载荷、签名
* 头部：声明 JWT 的类型（通常是JWT）和使用的签名算法（如 HS256、RS256 等）。
* 存储需要传递的核心信息（称为 “声明”，Claims），包括标准声明和自定义声明。
* 使用 Header 中指定的签名算法，对 “编码后的 Header” 和 “编码后的 Payload” 进行签名
 * */
public class JWTUtils {

    private static final String key = "zH4NRP1HMALxxCFnFh27KB7r7g3kVRkQjUI2WvQk0U0=";
    //设置令牌有效期为七天
    private static final long EXPIRATION = 7 * 24 * 60 * 60 * 1000;

    private static SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 生成JWT令牌
     * @param id  用户id
     * @return 生成的JWT令牌
     */
    public static String generateToken(Long id,String role) {
        Map<String,Object> claims = new HashMap<>();
        claims.put("id",id);
        claims.put("role",role);
        String JWT = Jwts.builder().
                addClaims(claims). //添加载荷信息
                signWith(getSecretKey()).//设置签名密钥
                setExpiration(new Date(System.currentTimeMillis()+EXPIRATION)).compact();//设置过期时间
        return JWT;
    }



    /**
     * 解析JWT令牌
     * @param token JWT令牌
     * @return 返回claims 键值对
     */
    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())//设置签名密钥
                .build()
                .parseClaimsJws(token)//接受token
                .getBody();
    }
}
