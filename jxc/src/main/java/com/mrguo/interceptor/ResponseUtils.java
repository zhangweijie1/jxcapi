package com.mrguo.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrguo.common.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class ResponseUtils {

    @Autowired
    private HttpServletResponse response;

    public void setResponse(Integer status,
                            String title) throws IOException {
        Result result = Result.fail(status, title, null);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(status);
        PrintWriter writer = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(result);
        writer.append(json);
        //重定向到URL
        //response.sendRedirect(targetUrl);
    }
}
