/*
package com.jzo2o.mysql.interceptor;

import com.jzo2o.common.handler.UserInfoHandler;
import com.jzo2o.common.model.CurrentUserInfo;
import com.jzo2o.common.utils.ObjectUtils;
import com.jzo2o.common.utils.ReflectUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;

import java.sql.SQLException;

import static com.jzo2o.mysql.constants.DbFiledConstants.CREATE_BY;
import static com.jzo2o.mysql.constants.DbFiledConstants.UPDATE_BY;

*/
/**
 * @author itcast
 *//*

public class MyBatisAutoFillInterceptor implements InnerInterceptor {

    private final UserInfoHandler userInfoHandler;

    public MyBatisAutoFillInterceptor(UserInfoHandler userInfoHandler) {
        this.userInfoHandler = userInfoHandler;
    }
    @Override
    public void beforeUpdate(Executor executor, MappedStatement ms, Object parameter) throws SQLException {
        // 批量操作兼容
        if (parameter instanceof Iterable) {
            for (Object obj : (Iterable<?>) parameter) {
                updateExe(obj);
                insertExe(ms, obj);
            }
        } else if (parameter != null && parameter.getClass().isArray()) {
            int length = java.lang.reflect.Array.getLength(parameter);
            for (int i = 0; i < length; i++) {
                Object obj = java.lang.reflect.Array.get(parameter, i);
                updateExe(obj);
                insertExe(ms, obj);
            }
        } else {
            updateExe(parameter);
            insertExe(ms, parameter);
        }
    }

    private void insertExe(MappedStatement ms, Object parameter){
        // 判断当前操作是否是插入
        if (ms.getSqlCommandType() == SqlCommandType.INSERT) {
            // 判断是否有 creator 字段
            if(ObjectUtils.isNotNull(parameter) && ReflectUtils.containField(CREATE_BY, parameter.getClass())){
                Long userId = currentUserId();
                if(ObjectUtils.isNotNull(userId)){
                    ReflectUtils.setFieldValue(parameter, CREATE_BY, userId);
                }
            }
        }
    }

    private void updateExe(Object parameter){
        // 判断是否有 updater 字段
        if(ObjectUtils.isNotNull(parameter) && ReflectUtils.containField(UPDATE_BY, parameter.getClass())){
            Long userId = currentUserId();
            if(ObjectUtils.isNotNull(userId)){
                ReflectUtils.setFieldValue(parameter, UPDATE_BY, userId);
            }
        }
    }

    private Long currentUserId() {
        if(ObjectUtils.isNull(userInfoHandler)){
            return null;
        }
        CurrentUserInfo currentUserInfo = userInfoHandler.currentUserInfo();
        return currentUserInfo != null ? currentUserInfo.getId() : null;
    }
}
*/
