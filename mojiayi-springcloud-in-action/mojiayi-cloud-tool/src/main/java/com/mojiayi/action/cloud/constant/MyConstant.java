package com.mojiayi.action.cloud.constant;

/**
 * <p>
 * 可以在多个模块间共用的常量
 * </p>
 *
 * @author mojiayi
 */
public class MyConstant {
    /**
     * 请求唯一id
     */
    public static final String TRACE_ID = "traceId";
    /**
     * 登录token
     */
    public static final String X_ACCESS_TOKEN = "X-Access-Token";
    /**
     * 可用作数据隔离的数据属性
     */
    public static final String DATA_PERMISSION = "dataPermission";
    /**
     * 会员id
     */
    public static final String MEMBER_ID = "memberId";
    /**
     * 会员名
     */
    public static final String MEMBER_NAME = "memberName";

    private MyConstant() {

    }
}
