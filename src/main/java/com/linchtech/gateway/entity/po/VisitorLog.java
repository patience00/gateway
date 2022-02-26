package com.linchtech.gateway.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 通天晓
 * @date 2018-10-05 21:19
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("visitor")
public class VisitorLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String ip;

    private Long time;

    @TableField("visit_time")
    private String visitTime;

    private String address;

}
