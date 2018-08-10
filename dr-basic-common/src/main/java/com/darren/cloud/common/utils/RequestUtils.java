package com.darren.cloud.common.utils;

import com.darren.cloud.common.regex.RegexType;
import eu.bitwalker.useragentutils.UserAgent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

/**
 * API、URL请求拦截通用工具类
 *
 * @author darren.ouyang
 * @version 2018/8/8 17:57
 */
@Slf4j
public class RequestUtils {

    private static final String URI_SEPARATOR = "/";
    private static final String MICROMESSENGER = "micromessenger";
    private static final String REQUEST_HEADER_USER_AGENT = "user-agent";
    private static final String REQUEST_HEADER_REFERER = "referer";
    private static final String URI_ZUUL = "zuul";

    /**
     * 参数密码过滤正则
     */
    private static final String PASSWORD_FILTER_REGEX =
            "(password=\\[([\\S\\s])*\\])|(\"password\":\"([\\S\\s])*\")|(newpwd=\\[([\\S\\s])*\\])|(\"newpwd\":\"([\\S\\s])*\")|(token=\\[([\\S\\s])*\\])|(\"token\":\"([\\S\\s])*\")|(accessToken=\\[([\\S\\s])*\\])|(\"accessToken\":\"([\\S\\s])*\")|(appSecret=\\[([\\S\\s])*\\])|(\"appSecret\":\"([\\S\\s])*\")";


    /**
     * 手机正则模式
     **/
    private static final Pattern PHONE_REGEX_PATTERN = Pattern.compile(RegexType.PHONE_TERMINAL_REGEX, Pattern.CASE_INSENSITIVE);
    /**
     * 平板正则模式
     **/
    private static final Pattern PAD_REGEX_PATTERN = Pattern.compile(RegexType.PAD_TERMINAL_REGEX, Pattern.CASE_INSENSITIVE);

    private static final String UNKNOWN = "unknown";

    /**
     * 得到当前{@code HttpServletRequest}
     * <p>
     * <b style="color:red"> 注意 : 只能在controller层中使用,service层 和 dao层 会报错 </b>
     *
     * @return 当前HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 判断请求是否为Ajax请求.
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    /**
     * 查询浏览器终端
     *
     * @param userAgent : 浏览器代理
     *
     *        <pre>
     *        request.getHeader("user-agent");
     *        </pre>
     * 
     * @return @see {@link TerminalEnum}
     */
    public static TerminalEnum queryTerminal(String userAgent) {
        if (null == userAgent) {
            return null;
        }
        userAgent = userAgent.toLowerCase();
        if (userAgent.indexOf(MICROMESSENGER) > 0) {
            return TerminalEnum.WX;
        }
        // 匹配
        Matcher matcherPhone = PHONE_REGEX_PATTERN.matcher(userAgent);
        Matcher matcherTable = PAD_REGEX_PATTERN.matcher(userAgent);
        if (matcherPhone.find() || matcherTable.find()) {
            return TerminalEnum.APP;
        } else {
            return TerminalEnum.PC;
        }
    }

    /**
     * 1.从uri中获取租户代码 uri默认格式：/msa-boss-server/{tenantCode}/users/list
     * 2.使用:该方法通过网关访问微服务用于获取租户代码,微服务内部之间的调用不经过该方法
     * 
     * 3.兼容:通过zuul上传文件使用(原因:上传文件通过网关转发到相关微服务后,获取文件原始名称中文名称乱码,同时zuul也支持大文件上传操作) 通过zuul上传文件使用示例URI:
     * /zuul/marketing/siyu/import-order-files/**
     * 
     * @param uri 格式：/msa-boss-server/{tenantCode}/users/list
     * @return
     */
    public static String getTenantCode(String uri) {
        if (StringUtils.isEmpty(uri)) {
            return null;
        }

        // 如uri以/开头的则截取掉/,
        if (uri.startsWith(URI_SEPARATOR)) {
            uri = uri.substring(1);
        }
        // 如果当前访问路径是以 zuul开头的URI 分组示例:[zuul,msa-marketing-server,siyu,list]
        if (uri.startsWith(URI_ZUUL)) {
            String[] separated = uri.split(URI_SEPARATOR);
            if (separated.length >= 3) {
                return separated[2];
            }
        } else {
            // 如果当前访问路径是不以 zuul开头的URI 分组示例:[msa-marketing-server,siyu,list]
            String[] separated = uri.split(URI_SEPARATOR);
            if (separated.length >= 2) {
                return separated[1];
            }
        }

        return null;
    }

    /**
     * 获取请求参数，返回map对象
     */
    public static Map<String, String> getParameters(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>(10);
        for (Map.Entry<String, String[]> param : request.getParameterMap().entrySet()) {
            // 参数key
            String paramKey = param.getKey();
            // 参数value(数组)
            String[] paramValues = param.getValue();
            StringBuilder paramValue = new StringBuilder("");
            for (int i = 0; i < paramValues.length; i++) {
                if (i == paramValues.length - 1) {
                    paramValue.append(paramValues[i]);
                } else {
                    paramValue.append(paramValues[i]).append(",");
                }
            }
            // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            params.put(paramKey, paramValue.toString());
        }
        return params;
    }

    /**
     * 得到域名,比如 : www.google.com
     *
     * @return www.xxx.com
     */
    public static String getRequestDomain(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).append("/").toString();
    }

    /**
     * 是否是 Content-Type=application/json; json传输
     */
    public static boolean isApplicationJsonHeader(HttpServletRequest request) {
        String contentType = request.getHeader(HttpHeaders.CONTENT_TYPE);
        return contentType != null
                && StringUtils.replaceAll(contentType.trim(), StringUtils.SPACE, StringUtils.EMPTY).contains(MediaType.APPLICATION_JSON_VALUE);
    }

    /**
     * 获取header参数
     */
    public static String getRequestHeader(String headerName) {
        return getRequest().getHeader(headerName);
    }

    /**
     * 得到 {@link #REQUEST_HEADER_USER_AGENT} 信息
     *
     * @return 描述信息
     */
    public static String getUserAgentHeader() {
        return getRequestHeader(REQUEST_HEADER_USER_AGENT);
    }

    /**
     * @return {@link eu.bitwalker.useragentutils.UserAgent}
     */
    public static UserAgent getUserAgent() {
        return UserAgent.parseUserAgentString(getUserAgentHeader());
    }

    /**
     * 得到请求地址
     *
     * @return 如果是在http://www.google.com域名下请求当前服务器某个api,比如:http://www.aidijing.com/api,
     *         <p>
     *         那么获取到的就是http://www.google.com这个地址,而不是http://www.aidijing.com/api
     */
    public static String getRequestReferrerUrl() {
        return getRequestHeader(REQUEST_HEADER_REFERER);
    }

    /**
     * 得到请求信息
     * <p>
     *
     * <pre>
     *  请求URL : /
     *  请求URI : http://localhost:8080/
     *  请求方式 : GET	同步请求
     *  请求者IP : 0:0:0:0:0:0:0:1
     *  请求时间 : 2017-06-04T12:07:05.575Z
     *  请求头内容 : host=localhost:8080
     *  请求头内容 : connection=keep-alive
     *  请求头内容 : upgrade-insecure-requests=1
     *  请求头内容 : user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36
     *  请求头内容 : accept=text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*\/*;q=0.8
     *  请求头内容 : accept-encoding=gzip, deflate, sdch, br
     *  请求头内容 : accept-language=zh-CN,zh;q=0.8,en;q=0.6
     *  请求头内容 : cookie=Hm_lvt_14b72f1e13e0586c5f38e5d1d16c549e=1480300179; Hm_lvt_3838faa5c4b1fd15b49f3dd6c90109b7=1480041314,1480057365,1480300285; Hm_lvt_263206af6573c0a362453bee56cd61b6=1480300319; Hm_lvt_e7019b8edb5edab3e54c5e229af23014=1480299450,1480557169,1480667339; Hm_lvt_69c2798f0b79a692b9d74581cf4bdef1=1480299476,1480650441,1481698113; b3log-latke="{\"userPassword\":\"f1c122ca4cf986be49d8e0922d9039b8\",\"userEmail\":\"yujunhao_8831@yahoo.com\"}"; Hm_lvt_9879c0fbf0414b572df462ca41b25798=1491013401; Webstorm-e3ef6d55=e16b3634-28fb-4ed9-9683-aa8c6f9d5bb9; tcd=a14fe7b56fe741d38dc3872a0ca159a9; 42a=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwaG9uZSI6IiIsIndhc0xvZ2luIjpmYWxzZSwibG9nbyI6Imh0dHA6Ly9yZXNvdXJjZS4xMzMyMi5jb20vdXNlci91c2VyLnBuZyIsImlkIjoyMzI5LCJ1c2VyTmFtZSI6ImExNGZlN2I1NmZlNzQxZDM4ZGMzODcyYTBjYTE1OWE5IiwidHlwZSI6IlVzZXIifQ.m6m28ayhoXX8pjenjxLZblZzxtS6HMqfjTGe_cDCTNQ; UM_distinctid=15bddd43267107a-0ca374e9b56a97-153d655c-1aeaa0-15bddd43268eac; CNZZDATA1260716016=355481192-1494074298-null%7C1494117924; _ga=GA1.1.205058624.1492656997
     *  请求参数 : username=[zhaoyc]
     * </pre>
     */
    public static String getRequestMessage(HttpServletRequest request) throws IOException {
        StringBuilder parameters = new StringBuilder();

        parameters.append("\n请求URI : ").append(request.getRequestURI())
                //
                .append("\n请求URL : ").append(request.getRequestURL())
                //
                .append("\n请求方式 : ").append(request.getMethod())
                //
                .append(RequestUtils.isAjaxRequest(request) ? "\tajax请求" : "\t同步请求")
                //
                .append("\n请求者IP : ").append(request.getRemoteAddr())
                //
                .append("\n请求者UserId : ").append(request.getHeader("userId"))
                //
                .append("\n请求时间 : ").append(Instant.now());
        final Enumeration<String> headerNames = request.getHeaderNames(); // 请求头
        while (headerNames.hasMoreElements()) {
            final String element = headerNames.nextElement();
            if (null != element) {
                String header = request.getHeader(element);
                parameters.append("\n请求头内容 : ").append(element).append("=").append(header);
            }
        }
        parameters.append("\n请求参数 : ").append(getRequestParametersWithPwd(request));
        final Enumeration<String> sessionAttributeNames = request.getSession().getAttributeNames(); // 请求Session内容
        while (sessionAttributeNames.hasMoreElements()) {
            parameters.append("\nSession内容 : ").append(sessionAttributeNames.nextElement());
        }

        // 非测试环境（非调试环境）密码、token、AccessToken不可见
        if (!log.isDebugEnabled()) {
            return StringUtils.replaceAll(parameters.toString(), PASSWORD_FILTER_REGEX, "******");
        } else {
            return parameters.toString();
        }
    }

    /**
     * 得到请求参数，不隐藏密码
     */
    private static String getRequestParametersWithPwd(HttpServletRequest request) throws IOException {
        StringBuilder parameters = new StringBuilder();

        // json参数
        if (RequestUtils.isApplicationJsonHeader(request)) {
            parameters.append(IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8.displayName()));
        }
        // 普通参数
        else {
            request.getParameterMap()
                    .forEach((String key, String[] values) -> parameters.append(key).append("=").append(Arrays.toString(values)).append("\t"));
        }

        return parameters.toString();

    }

    /**
     * 得到请求参数，非调试模式隐藏密码
     */
    public static String getRequestParameters(HttpServletRequest request) throws IOException {
        StringBuilder parameters = new StringBuilder();

        // json参数
        if (RequestUtils.isApplicationJsonHeader(request)) {
            parameters.append(IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8.displayName()));
        }
        // 普通参数
        else {
            request.getParameterMap()
                    .forEach((String key, String[] values) -> parameters.append(key).append("=").append(Arrays.toString(values)).append("\t"));
        }

        // 非测试环境（非调试环境）密码、token、AccessToken不可见
        if (!LogManager.getLogger().isDebugEnabled()) {
            return StringUtils.replaceAll(parameters.toString(), PASSWORD_FILTER_REGEX, "******");
        } else {
            return parameters.toString();
        }

    }

    /**
     * 获取pathVariable参数值
     */
    @SuppressWarnings("unchecked")
    public static String getPathVariableValue(HttpServletRequest request, String pathVariable) {
        Object obj = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (obj != null && obj instanceof Map) {
            Map<Object, Object> pathVariables = (Map<Object, Object>) obj;
            Object value = pathVariables.get(pathVariable);
            return value == null ? null : value.toString();
        }

        return null;
    }

    /**
     * 得到当前请求的ip地址
     * <p>
     * <b style="color:red"> 注意 : 只能在controller层中使用,service层 和 dao层 会报错 </b>
     */
    public static String getRequestIp() {
        HttpServletRequest request = getRequest();
        String ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotBlank(ip) && !UNKNOWN.equalsIgnoreCase(ip)) {
            return ip.trim();
        }
        ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotBlank(ip) && !UNKNOWN.equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为 真实 ip
            return StringUtils.split(ip, ",")[0].trim();
        }
        ip = request.getHeader("Proxy-Client-IP");
        if (StringUtils.isNotBlank(ip) && !UNKNOWN.equalsIgnoreCase(ip)) {
            return ip.trim();
        }
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (StringUtils.isNotBlank(ip) && !UNKNOWN.equalsIgnoreCase(ip)) {
            return ip.trim();
        }
        ip = request.getHeader("HTTP_CLIENT_IP");
        if (StringUtils.isNotBlank(ip) && !UNKNOWN.equalsIgnoreCase(ip)) {
            return ip.trim();
        }
        ip = request.getHeader("X-Cluster-Client-IP");
        if (StringUtils.isNotBlank(ip) && !UNKNOWN.equalsIgnoreCase(ip)) {
            return ip.trim();
        }
        return request.getRemoteAddr();
    }


    /**
     * 终端
     */
    public enum TerminalEnum {
        //
        WX, //
        PC, //
        APP
    }

}
