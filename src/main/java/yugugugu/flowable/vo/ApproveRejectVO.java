package yugugugu.flowable.vo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author liuyu
 * @date 2023/8/27
 **/
@Data
public class ApproveRejectVO {
    private String taskId;
    private Boolean approve;
    private String name;
}

