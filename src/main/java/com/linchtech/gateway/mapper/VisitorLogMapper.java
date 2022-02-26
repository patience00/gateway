package com.linchtech.gateway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linchtech.gateway.entity.po.VisitorLog;
import com.linchtech.gateway.entity.vo.VisitorVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author 107
 * @date 2018-10-06 21:51
 **/
@Repository
@Mapper
public interface VisitorLogMapper extends BaseMapper<VisitorLog> {
    VisitorVO count();
}
