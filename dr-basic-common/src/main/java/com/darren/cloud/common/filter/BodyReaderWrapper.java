package com.darren.cloud.common.filter;

import com.darren.cloud.common.utils.RequestUtils;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.springframework.util.StreamUtils;


/**
 * 解决流不能二次读取问题. 对于Content-Type=application/json;请求需要解析其中的参数. 这样在Controller中@RequestBody也可以获取到参数了.
 *
 * @author darren.ouyang
 * @version 2018/8/9 12:25
 */
public class BodyReaderWrapper extends HttpServletRequestWrapper {

    private final byte[] body;

    public BodyReaderWrapper(HttpServletRequest request) throws IOException {
        super(request);
        if (RequestUtils.isApplicationJsonHeader(request)) {
            body = StreamUtils.copyToByteArray(request.getInputStream());
        } else {
            body = null;
        }
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (null == body) {
            return super.getInputStream();
        }
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return inputStream.read();
            }
        };
    }

}
