package com.mrguo.service.impl.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/7/309:30 AM
 * @updater 郭成兴
 * @updatedate 2020/7/309:30 AM
 */
@Service
public class ExcleServieUtils {

    @Autowired
    private HttpServletResponse response;

    public void stream2excle(String fileName, ByteArrayOutputStream outputStream) throws IOException {
        byte[] bytes = outputStream.toByteArray();
        fileName = URLEncoder.encode(fileName, "utf-8");
        response.reset();
        // 解决跨域问题，这句话是关键，对任意的域都可以，如果需要安全，可以设置成安前的域名
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        //
        response.setHeader("Content-Length", String.valueOf(bytes.length));
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setContentType("application/msexcel;charset=utf-8");
        //
        response.getOutputStream().write(bytes);
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }
}
