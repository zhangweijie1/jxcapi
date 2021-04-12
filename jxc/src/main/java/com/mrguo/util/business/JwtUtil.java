package com.mrguo.util.business;

import io.jsonwebtoken.*;
import lombok.Data;

import java.util.Date;

/**
 * @author 郭成兴
 * @ClassName JwtUtil
 * @Description JwtUtil token工具类
 * @date 2019/4/11 3:58 PM
 * @updater 郭成兴
 * @updatedate 2019/4/11 3:58 PM
 */
@Data
public class JwtUtil {

    private static String KEY = "mrguo512830037qq";
    /**
     * token时间 毫秒
     * 一个小时
     */
    private static long TTL = 86400000;

    /**
     * 生成JWT
     *
     * @param userId   用户ID
     * @return
     */
    public static String createJWT(String userId) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder()
                // 唯一KEY
                .setId(userId)
                // 签发时间
                .setIssuedAt(now)
                // 设置签名使用的签名算法和签名使用的秘钥
                .signWith(SignatureAlgorithm.HS256, KEY);
        if (TTL > 0) {
            // 设置过期时间
            builder.setExpiration(new Date(nowMillis + TTL));
        }
        return builder.compact();
    }

    /**
     * 解析JWT
     *
     * @param jwtStr
     * @return
     */
    public static Claims parseJWT(String jwtStr) {
        return Jwts.parser()
                .setSigningKey(KEY)
                .parseClaimsJws(jwtStr)
                .getBody();
    }
}
