package com.darren.cloud.common;

import com.darren.cloud.common.utils.JsonUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.LinkedHashMap;
import lombok.ToString;

/**
 * 请求返回对象
 *
 * @author darren.ouyang
 * @version 2018/8/8 17:57
 * @param <T>
 */
@ToString
public class ResponseEntity<T> {

    /** 通配符 **/
    private static final String WILDCARD_ALL = "*";
    /** 响应状态码 **/
    private volatile String statusCode = StatusCode.BAD_REQUEST.getStatusCode();
    /** 响应状态码对应的提示信息 **/
    private volatile String statusMessage = StatusCode.BAD_REQUEST.getStatusMessage();
    /** 响应内容 **/
    private volatile T responseContent = null;

    /**
     * json处理时需要过滤的字段,默认不过滤 具体看 {@link JsonUtils#toFilterJson}
     */
    @JsonIgnore
    private volatile String filterFields = WILDCARD_ALL;

    /**
     * 自定义导出标题，key需与数据对象的属性名对应
     */
    @JsonIgnore
    private volatile LinkedHashMap<String, String> exportTitleMap;

    /**
     * 
     */
    public ResponseEntity() {}

    /**
     * 
     * @param statusCode 状态码
     * @param statusMessage 状态信息
     */
    private ResponseEntity(final String statusCode, final String statusMessage) {
        this(statusCode, statusMessage, null);
    }

    /**
     * 
     * @param statusCode 状态码
     * @param statusMessage 状态信息
     * @param responseContent 返回的结果
     */
    private ResponseEntity(final String statusCode, final String statusMessage, final T responseContent) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.responseContent = responseContent;
    }

    /**
     * 请求成功，状态码为20000
     * 
     * @return
     */
    public ResponseEntity<T> ok() {
        return ok(StatusCode.OK.getStatusCode(), StatusCode.OK.getStatusMessage());
    }

    /**
     * 请求成功，状态码为20000
     * 
     * @param message 提示消息
     * @return
     */
    public ResponseEntity<T> ok(final String message) {
        return ok(StatusCode.OK.getStatusCode(), message);
    }

    /**
     * 成功请求,成功状态码自行指定
     *
     * @param okStatusCode 成功状态码
     * @param message 提示消息
     * @return
     */
    public ResponseEntity<T> ok(final String okStatusCode, final String message) {
        return new ResponseEntity<T>(okStatusCode, message);
    }

    /**
     * 成功请求,成功状态码自行指定
     *
     * @param okStatusCode 成功状态码
     * @param message 提示消息
     * @return
     */
    public ResponseEntity<T> ok(final StatusCode okStatusCode, final String message) {
        return new ResponseEntity<T>(okStatusCode.getStatusCode(), message);
    }

    /**
     * 服务器发生错误，用户将无法判断发出的请求是否成功，状态码：20500
     * 
     * @param message 提示消息
     * @return
     */
    public ResponseEntity<T> systemError(final String message) {
        return new ResponseEntity<T>(StatusCode.SYSTEM_ERROR.getStatusCode(), message);
    }

    /**
     * 服务器发生错误，用户将无法判断发出的请求是否成功
     * 
     * @param errorStatusCode 错误状态码
     * @param message 提示消息
     * @return
     */
    public ResponseEntity<T> systemError(final StatusCode errorStatusCode, final String message) {
        return new ResponseEntity<T>(errorStatusCode.getStatusCode(), message);
    }

    /**
     * 失败请求，状态码：20400
     *
     * @return
     */
    public ResponseEntity<T> badRequest() {
        return badRequest(StatusCode.BAD_REQUEST.getStatusCode(), StatusCode.BAD_REQUEST.getStatusMessage());
    }

    /**
     * 失败请求，状态码：20400
     *
     * @param message 提示消息
     * @return
     */
    public ResponseEntity<T> badRequest(final String message) {
        return badRequest(StatusCode.BAD_REQUEST.getStatusCode(), message);
    }

    /**
     * 失败请求,失败状态码自行指定
     *
     * @param failStatusCode 失败状态码
     * @param message 提示消息
     * @return
     */
    public ResponseEntity<T> badRequest(final String failStatusCode, final String message) {
        return new ResponseEntity<T>(failStatusCode, message);
    }

    /**
     * 失败请求,失败状态码自行指定
     *
     * @param failStatusCode 失败状态码
     * @param message 提示消息
     * @return
     */
    public ResponseEntity<T> badRequest(final StatusCode failStatusCode, final String message) {
        return new ResponseEntity<T>(failStatusCode.getStatusCode(), message);
    }

    /**
     * 表示用户得到授权，但权限不足，状态码：20403
     * 
     * @return
     */
    public ResponseEntity<T> forbidden() {
        return forbidden(StatusCode.FORBIDDEN.getStatusMessage());
    }

    /**
     * 表示用户得到授权，但权限不足，状态码：20403
     * 
     * @param message 提示消息
     * @return
     */
    public ResponseEntity<T> forbidden(final String message) {
        return new ResponseEntity<T>(StatusCode.FORBIDDEN.getStatusCode(), message);
    }

    /**
     * 请求参数出错，状态码：20107
     * 
     * @return
     */
    public ResponseEntity<T> badValidated() {
        return forbidden(StatusCode.BAD_VALIDATED.getStatusMessage());
    }

    /**
     * 请求参数出错，状态码：20107
     * 
     * @param message 提示消息
     * @return
     */
    public ResponseEntity<T> badValidated(final String message) {
        return new ResponseEntity<T>(StatusCode.BAD_VALIDATED.getStatusCode(), message);
    }

    /**
     * 表示用户没有权限（令牌、用户名、密码错误），状态码：20401
     * 
     * @return
     */
    public ResponseEntity<T> unauthorized() {
        return unauthorized(StatusCode.UNAUTHORIZED.getStatusMessage());
    }

    /**
     * 表示用户没有权限（令牌、用户名、密码错误），状态码：20401
     * 
     * @param message 提示消息
     * @return
     */
    public ResponseEntity<T> unauthorized(final String message) {
        return new ResponseEntity<T>(StatusCode.UNAUTHORIZED.getStatusCode(), message);
    }



    /**
     * 非分页-设置过滤字段
     * <p>
     * <b style="color:red">注意只会过滤 responseContent 中的内容</b>
     *
     * @param filterFields : 过滤字段，多个逗号隔开,{@link JsonUtils#toFilterJson(Object , String)}
     * @return this
     */
    public ResponseEntity<T> setFilterFields(final String filterFields) {
        if (null == filterFields || WILDCARD_ALL.equals(filterFields)) {
            return this;
        }
        StringBuilder builder = new StringBuilder(WILDCARD_ALL).append(",responseContent[");
        builder.append(filterFields.trim()).append("]");
        this.filterFields = builder.toString();
        return this;
    }


    /**
     * 分页结果-设置过滤字段
     * <p>
     * <b style="color:red">注意只会过滤 responseContent 中list下的内容</b>
     * <p>
     * 默认指定为{@link com.github.pagehelper.PageInfo#list}
     *
     * @param filterFields : 过滤字段，多个逗号隔开,{@link JsonUtils#toFilterJson(Object , String)}
     * @return this
     */
    public ResponseEntity<T> setFilterFieldsForPaging(final String filterFields) {
        if (null == filterFields || WILDCARD_ALL.equals(filterFields)) {
            return this;
        }
        StringBuilder builder = new StringBuilder(WILDCARD_ALL).append(",responseContent[");
        // 分页对象,则只对分页对象内的结果集进行处理
        builder.append("*,list[").append(filterFields.trim()).append("]]");
        this.filterFields = builder.toString();
        return this;
    }

    /**
     * {@link #setFilterFieldsAndFlush(String , boolean)} 默认不是过滤分页结果集
     */
    public ResponseEntity<T> setFilterFieldsAndFlush(final String filterFields) {
        return this.setFilterFieldsAndFlush(filterFields, false);
    }

    /**
     * 设置过滤字段并过滤刷新
     * <p>
     * <b style="color:red"> 注意该方法在controller中最后 <code>return</code> 时使用,可能会导致flush2次,因为在自定义
     * <p>
     * {@link org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice}
     * 的实现类中,会再次flush(因为我在返回的时候会进行flush);
     * <p>
     * 如果,你未定义 {@link org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice}
     * 实现类那么就需要在 <code>return</code> 时调用 {@link #setFilterFieldsAndFlush(String , boolean)} 而不是
     * {@link #setFilterFields(String)}
     * <p>
     * 亦或者你自定义了实现类,但是没有进行flush,那么你还是得调用 {@link #setFilterFieldsAndFlush(String , boolean)} </b>
     * <p>
     * 当然我可以设置<code>boolean</code>状态值,但是我又不想这个状态值返回到外面,那么这样就需要对这个字段进行忽略,这样就不会进行序列化.
     * <p>
     * 但是有的时候在传输的时候是需要序列号和反序列化的,所以这里是使用约定而不是既定
     * <p>
     *
     * @param filterFields : 需要过滤的字段
     * @param isFilterPaging : 过滤的结果集是否存在分页, 更多 {@link #setFilterFields(String)}
     *        {@link #setFilterFieldsForPaging(String)}
     * @return
     */
    public ResponseEntity<T> setFilterFieldsAndFlush(final String filterFields, boolean isFilterPaging) {
        if (isFilterPaging) {
            return this.setFilterFieldsForPaging(filterFields).filterFieldsFlush();
        }
        return this.setFilterFields(filterFields).filterFieldsFlush();
    }

    /**
     * 过滤字段刷新
     *
     * @return 刷新后的 <code>this</code>
     */
    public ResponseEntity<T> filterFieldsFlush() {
        return JsonUtils.jsonToType(this.toJson(), new TypeReference<ResponseEntity<T>>() {});
        // return JsonUtils.jsonToType(this.toJson(), this.getClass());
    }

    /**
     * 对<code>this</code>进行json序列号,如果设置了过滤字段则会进行过滤
     *
     * @return json
     */
    public String toJson() {
        if (this.isNotFieldsFilter()) {
            return JsonUtils.toJson(this);
        }
        return JsonUtils.toFilterJson(this, this.getFilterFields());
    }


    /**
     * 是否成功
     *
     * @return 如果状态 <b style="color:red">是<code>StatusCode.OK</code></b> 则返回 <code>true</code>
     */
    @JsonIgnore
    public boolean isOk() {
        return this.getStatusCode() != null && StatusCode.OK.getStatusCode().equals(this.getStatusCode());
    }

    /**
     * 是否不成功
     *
     * @return "!" {@link #isOk()}
     */
    @JsonIgnore
    public boolean isNotOk() {
        return !isOk();
    }

    /**
     * 是否需要过滤字段
     *
     * @return "!" {@link #isNotFieldsFilter()}
     */
    @JsonIgnore
    public boolean isFieldsFilter() {
        return !this.isNotFieldsFilter();
    }

    /**
     * 是否不需要过滤字段
     *
     * @return 如果 <b style="color:red"> null == this.getFilterFields() || {@link #filterFields}
     *         <code>equals</code> {@link #WILDCARD_ALL} </b>则返回 <code>true</code>
     */
    @JsonIgnore
    public boolean isNotFieldsFilter() {
        return null == this.getFilterFields() || WILDCARD_ALL.equals(this.getFilterFields());
    }


    /**
     * 获取返回对象
     * 
     * @return
     */
    public T getResponseContent() {
        return responseContent;
    }

    /**
     * 设置返回对象
     * 
     * @param responseContent
     * @return
     */
    public ResponseEntity<T> setResponseContent(T responseContent) {
        this.responseContent = responseContent;
        return this;
    }

    /**
     * 获取导出标题列
     * 
     * @return
     */
    public LinkedHashMap<String, String> getExportTitleMap() {
        return exportTitleMap;
    }

    /**
     * 设置导出标题列
     * 
     * @param exportTitleMap
     * @return
     */
    public ResponseEntity<T> setExportTitleMap(LinkedHashMap<String, String> exportTitleMap) {
        this.exportTitleMap = exportTitleMap;
        return this;
    }

    /**
     * 获取状态码
     * 
     * @return
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * 获取状态信息
     * 
     * @return
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * 获取过滤字段
     * 
     * @return
     */
    public String getFilterFields() {
        return filterFields;
    }

    /**
     * 业务状态码
     * 
     * @author zhaoyc
     * @version 创建时间：2018年1月12日 下午3:20:43
     */
    public enum StatusCode {
        /** 请求成功 **/
        OK("20000", "请求成功"),
        /** 需要修改密码 **/
        NEED_UPDATE_PASSWORD("20101", "需要修改密码"),
        /** 请求参数出错 **/
        BAD_VALIDATED("20107", "请求参数出错"),
        /** 请求失败 **/
        BAD_REQUEST("20400", "请求失败"),
        /** 身份验证失败 **/
        UNAUTHORIZED("20401", "身份验证失败"),
        /** 权限不足 **/
        FORBIDDEN("20403", "权限不足"),

        /** 系统内部错误 **/
        SYSTEM_ERROR("20500", "系统内部错误");

        private final String statusCode;
        private final String statusMessage;

        StatusCode(String statusCode, String statusMessage) {
            this.statusCode = statusCode;
            this.statusMessage = statusMessage;
        }

        public String getStatusMessage() {
            return statusMessage;
        }

        public String getStatusCode() {
            return statusCode;
        }

    }

}
