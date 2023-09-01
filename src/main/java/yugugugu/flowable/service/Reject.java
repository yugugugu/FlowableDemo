package yugugugu.flowable.service;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

/**
 * @author liuyu
 * @date 2023/9/1
 **/
public class Reject implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("申请不通过:"+execution.getVariables());
    }
}
