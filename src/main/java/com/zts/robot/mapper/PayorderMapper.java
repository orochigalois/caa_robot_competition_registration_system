package com.zts.robot.mapper;

import com.zts.robot.pojo.Payorder;
import com.zts.robot.pojo.PayorderKey;

public interface PayorderMapper extends WritePayorderMapper{
    int deleteByPrimaryKey(PayorderKey key);

    int insert(Payorder record);

    int insertSelective(Payorder record);

    Payorder selectByPrimaryKey(PayorderKey key);

    int updateByPrimaryKeySelective(Payorder record);

    int updateByPrimaryKey(Payorder record);


}