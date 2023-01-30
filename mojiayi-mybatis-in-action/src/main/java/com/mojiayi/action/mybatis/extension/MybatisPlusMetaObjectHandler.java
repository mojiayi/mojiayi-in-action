package com.mojiayi.action.mybatis.extension;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.mojiayi.action.mybatis.constants.MyConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * <p>
 * 元对象处理器，自动注入创建人、创建时间、修改人、修改时间
 * </p>
 *
 * @author guangri.li
 * @since 2023/1/28 22:36
 */
@Slf4j
@Component
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        long userId = getOperatorId();
        this.setFieldValByName("createBy", userId, metaObject);
        this.setFieldValByName("updateBy", userId, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        long userId = getOperatorId();
        this.setFieldValByName("updateBy", userId, metaObject);
    }

    private long getOperatorId() {
        String userId = MDC.get(MyConstants.OPERATOR_USER_ID);
        if (!StringUtils.hasLength(userId)) {
            log.info("本地线程空间中没有操作人id");
            return 0;
        }
        return Long.parseLong(userId);
    }
}
