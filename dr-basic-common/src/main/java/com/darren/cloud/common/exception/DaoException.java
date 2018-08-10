package com.darren.cloud.common.exception;

/**
 * dao异常
 *
 * @author darren.ouyang
 * @version 2018/8/8 17:57
 */
public class DaoException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -1472557644967847434L;

    public DaoException() {
        super();
    }

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoException(Throwable cause) {
        super(cause);
    }

    protected DaoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
