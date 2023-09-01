package yugugugu.flowable.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author liuyu
 * @date 2023/8/27
 **/
@Data
public class HistoryInfo {
    private String name;
    private String reason;
    private Integer days;
    private Boolean status;
    private Date startTime;
    private Date endTime;
}
