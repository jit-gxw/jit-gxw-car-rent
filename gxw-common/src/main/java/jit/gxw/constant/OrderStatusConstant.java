package jit.gxw.constant;

/**
 * 状态常量，启用或者禁用
 */
public class OrderStatusConstant {

    //待付押金
    public static final Integer PENDINGDEPOSIT = 0;

    //待取车
    public static final Integer WAITINGFORCOLLECTION = 1;
    //租赁中
    public static final Integer RENTING = 2;
    //已还车
    public static final Integer RETURNED = 3;
    //待付款
    public static final Integer WAITINGFORPAYMENT = 4;
    //已完成
    public static final Integer COMPLETED = 5;
    //已取消
    public static final Integer CANCELED = 6;
}
