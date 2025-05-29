package com.jzo2o.mysql.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.jzo2o.common.handler.UserInfoHandler;
import com.jzo2o.common.model.CurrentUserInfo;
import com.jzo2o.common.utils.ObjectUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * 自动填充字段
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    private final UserInfoHandler userInfoHandler;

    public MyMetaObjectHandler(UserInfoHandler userInfoHandler) {
        this.userInfoHandler = userInfoHandler;
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createBy", Long.class, getCurrentUserId());
        this.strictInsertFill(metaObject, "updateBy", Long.class, getCurrentUserId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateBy", Long.class, getCurrentUserId());
    }

    private Long getCurrentUserId() {
        if(ObjectUtils.isNull(userInfoHandler)){
            return null;
        }
        CurrentUserInfo currentUserInfo = userInfoHandler.currentUserInfo();
        return currentUserInfo != null ? currentUserInfo.getId() : null;
    }
}