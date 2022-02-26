package com.linchtech.gateway.entity.vo;

import lombok.Data;

/**
 * @author: 107
 * @date: 2018/10/10 9:59
 * @description:
 */
@Data
public class VisitorVO {

    /**总访问次数*/
    private Integer time;
    /**总访问人数*/
    private Integer count;
    /**
     * 访问地址
     */
    private IPVO address;
}
