package com.mrguo.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Md5Hash;

import java.util.UUID;

/**
 * @author 郭成兴
 * @ClassName: IDUtil
 * @Description: id工具类
 * @date 2018/10/30下午4:55
 * @updater 郭成兴
 * @updatedate 2018/10/30下午4:55
 */
@Slf4j
public class IDUtil {

    private static IdWorker idWorker = new IdWorker(0, 1);

    public static Long getSnowflakeId() {
        return idWorker.nextId();
    }

    public static String getuuid() {
        String id = String.valueOf(UUID.randomUUID());
        id = id.replaceAll("-", "");
        return id;
    }

    /**
     * 密码加密
     *
     * @param
     * @return
     * @throws
     * @author 郭成兴
     * @createdate 2019/4/14 1:30 PM
     * @updater 郭成兴
     * @updatedate 2019/4/14 1:30 PM
     */
    public static String encryptPassword(String pas, String salt) {
        return new Md5Hash(pas, salt, 3).toString();
    }

    public static void main(String[] args) {

    }
}
