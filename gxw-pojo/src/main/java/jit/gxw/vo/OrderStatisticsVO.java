package jit.gxw.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderStatisticsVO implements Serializable {
    //待付押金
    private Integer pendingDeposit;

    //待取车
    private Integer waitingForCollection;

    //租赁中
    private Integer renting;

    //已还车
    private Integer returned;

    //待付款
    private Integer waitingForPayment;
}
