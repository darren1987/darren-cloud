package com.darren.cloud.common.advice;

import com.darren.cloud.common.ResponseEntity;
import com.darren.cloud.common.exception.DaoException;
import com.darren.cloud.common.exception.ServiceException;
import javax.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 请求controller全局拦截控制器增强
 *
 * @author darren.ouyang
 * @version 2018/8/9 11:25
 */
@Slf4j
public class GlobalResponseControllerAdvice implements ResponseBodyAdvice<ResponseEntity<?>> {

    /**
     * 拦截自定义的ValidationException异常
     *
     * @param e 异常
     * @return 结果
     */
    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<?> validationExceptionHandler(ValidationException e) {
        return new ResponseEntity<>().badValidated(e.getMessage());
    }

    /**
     * 拦截自定义的service异常
     *
     * @param e 异常
     * @return 结果
     */
    @ExceptionHandler(value = ServiceException.class)
    public ResponseEntity<?> serviceExceptionHandler(ServiceException e) {
        log.error("Service 异常", e);
        return new ResponseEntity<>().badRequest(e.getMessage());
    }

    /**
     * 拦截自定义的dao异常
     *
     * @param e 异常
     * @return 结果
     */
    @ExceptionHandler(value = DaoException.class)
    public ResponseEntity<?> daoExceptionHandler(DaoException e) {
        log.error("Dao 异常", e);
        return new ResponseEntity<>().badRequest(e.getMessage());
    }

    /**
     * HTTP方法异常调用
     *
     * @param e 异常
     * @return 结果
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) {
        log.error("Http请求方法调用方式异常", e);
        return new ResponseEntity<>().systemError(e.getMessage());
    }

    /**
     * 拦截自定义以外的异常
     *
     * @param e 异常
     * @return 结果
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> exceptionHandler(Exception e) {
       log.error("系统发生异常,异常如下", e);
        return new ResponseEntity<>().systemError("服务器内部异常,请稍后重试");
    }


    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return ResponseEntity.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public ResponseEntity<?> beforeBodyWrite(ResponseEntity<?> body, MethodParameter returnType, MediaType selectedContentType,
        Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        // | 1. 字段过滤处理 | 如果自行设置了,那么就以自行设置的为主
        if (body.isFieldsFilter()) {
            body = body.filterFieldsFlush();
        }

        return body;
    }

}
