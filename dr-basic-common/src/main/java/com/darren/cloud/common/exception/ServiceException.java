package com.darren.cloud.common.exception;

/**
 * service异常
 *
 * @author darren.ouyang
 * @version 2018/8/8 17:57
 */
public class ServiceException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -81603026434363015L;

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    protected ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
