package com.linchtech.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.linchtech.gateway.entity.po.AccessUser;
import com.linchtech.gateway.entity.po.VisitorLog;
import com.linchtech.gateway.entity.vo.IPVO;
import com.linchtech.gateway.mapper.VisitorLogMapper;
import com.linchtech.gateway.util.DateUtil;
import com.linchtech.gateway.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 107
 * @date 2020-06-20 23:08
 **/
@Slf4j
@Component
public class VisitFilter implements GlobalFilter, Ordered {
    public static final String ACCESS_USER_INFO = "access-user-info";

    @Autowired
    private VisitorLogMapper visitorLogMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${spring.profiles.active}")
    private String active;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (!active.equals("dev")) {
            List<String> headerValue = request.getHeaders().get("X-Forwarder-For");
            log.info("nginx ip :{}", headerValue);
            String ip = headerValue.get(0);

            // ???????????????????????????
            VisitorLog visitorLog = new VisitorLog();
            // TODO ?????????session,????????????????????????,?????????????????????;???sessionId??????redis,1????????????
            String getSessionById = redisTemplate.opsForValue().get("SessionID:" + ip);
            IPVO ipAddress;
            String region = "";
            if (getSessionById == null) {
                // ??????session?????????session,???????????????
                redisTemplate.opsForValue().set("SessionID:" + ip, ip, 15, TimeUnit.MINUTES);
                visitorLog.setIp(ip);
                try {
                    ipAddress = getIpAddress(ip);
                    redisTemplate.opsForValue().set("IP_Addr:" + ip, JSON.toJSONString(ipAddress), 15, TimeUnit.MINUTES);
                    region = ipAddress.getData().getIp_infos().get(0).getRegion();
                    log.info("first time enter,region:{}", region);
                    visitorLog.setAddress(region);
                    visitorLog.setTime(System.currentTimeMillis());
                    visitorLog.setVisitTime(DateUtil.getDateString(System.currentTimeMillis()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                visitorLogMapper.insert(visitorLog);
            } else {
                String ips = redisTemplate.opsForValue().get("IP_Addr:" + ip);
                if (ips == null) {
                    region = "??????";
                } else {
                    IPVO ipvo = JSON.parseObject(ips, IPVO.class);
                    region = ipvo.getData().getIp_infos().get(0).getRegion();
                }
                log.info("second time enter,region:{}", region);
            }
            //?????????????????????
            AccessUser accessUser = AccessUser.builder()
                    .ip(ip)
                    .location(new String(region.getBytes(), StandardCharsets.UTF_8))
                    .build();
            try {
                request.mutate().header(ACCESS_USER_INFO, URLEncoder.encode(JSON.toJSONString(accessUser), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        return chain.filter(exchange.mutate().request(request).build());
    }

    @Override
    public int getOrder() {
        return -10;
    }


    private IPVO getIpAddress(String ip) throws IOException {
        HttpClient client = new HttpClient();
        log.info("ip:{}", ip);
        PostMethod getMethod = new PostMethod("http://www.geaping.com:9000/member/ip_query?domain=" + ip);
        getMethod.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                "(KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36");
        getMethod.addRequestHeader("Accept", "*/*"); // ????????????
        getMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8"); // ????????????
        client.getHttpConnectionManager().getParams().setConnectionTimeout(60000);
        client.getHttpConnectionManager().getParams().setSoTimeout(60000);
        // ????????????
        client.executeMethod(getMethod);
        InputStream inputStream = getMethod.getResponseBodyAsStream();
        String s = Utils.convertStreamToString(inputStream);
        log.info("ip????????????:" + s);
        if (StringUtils.isEmpty(s)) {
            log.info("ip:{},????????????",ip);
            return IPVO.builder().data(
                    IPVO.DataBean.builder().ip_infos(
                            Arrays.asList(IPVO.DataBean.IpInfosBean.builder().region("????????????").build()
                            )).build()
            ).build();
        }
        return JSON.parseObject(s.replace("/n", ""), IPVO.class);
    }
}
