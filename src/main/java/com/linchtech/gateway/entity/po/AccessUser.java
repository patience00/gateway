package com.linchtech.gateway.entity.po;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: 107
 * @date: 2019/2/20 14:43
 * @description:
 * @Review:
 */
@Data
@Builder
public class AccessUser implements Serializable {

    private Long userId;
    private String location;
    private String ip;

    private String requestUri;
    private String method;
    private String param;

}
