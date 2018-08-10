package com.darren.cloud.zuul.fegin;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * 鉴权接口 dr-auth-server
 *
 * @author darren.ouyang
 * @version 2018/8/9 09:53
 */
@FeignClient(value = "dr-auth-server", fallback = AuthFeignClientHystrix.class)
public interface AuthFeignClient {

}
