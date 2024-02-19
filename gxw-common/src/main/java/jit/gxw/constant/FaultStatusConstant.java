package jit.gxw.constant;

/**
 * 状态常量，启用或者禁用
 */
public class FaultStatusConstant {

    //已报修
    public static final Integer DECLARED = 0;

    //维修中
    public static final Integer REPAIRING = 1;
    //维修完成
    public static final Integer COMPLETED = 2;
    //维修失败
    public static final Integer FAILED = 3;

}
