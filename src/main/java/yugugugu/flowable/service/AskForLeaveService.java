package yugugugu.flowable.service;

import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yugugugu.flowable.entity.HistoryInfo;
import yugugugu.flowable.utils.RespBean;
import yugugugu.flowable.vo.ApproveRejectVO;
import yugugugu.flowable.vo.AskFroLeaveVO;

import java.util.*;

/**
 * @author liuyu
 * @date 2023/8/27
 **/
@Service
public class AskForLeaveService {
    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    HistoryService historyService;

    public RespBean askForLeave(AskFroLeaveVO leaveVo){
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("name",leaveVo.getName());
        paramsMap.put("days",leaveVo.getDays());
        paramsMap.put("name",leaveVo.getReason());
        try {
            runtimeService.startProcessInstanceByKey("holidayRequest",leaveVo.getName(),paramsMap);
            return RespBean.ok("提交请假流程成功");
        }catch(Exception e){
            e.printStackTrace();
        }
        return RespBean.error("提交流程失败");
    }


    public RespBean leaveList(String identity) {
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(identity).list();
        List<Map<String,Object>> list = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            Map<String, Object> variables = taskService.getVariables(task.getId());
            variables.put("id", task.getId());
            list.add(variables);
        }
        return RespBean.ok("加载成功", list);
    }

    public RespBean askForLeaveHandler(ApproveRejectVO approveRejectVO) {
        try {
            boolean approved = approveRejectVO.getApprove();
            Map<String,Object> paramsMap = new HashMap<>();
            paramsMap.put("approved",approved);
            paramsMap.put("employee",approveRejectVO.getName());
            Task task = taskService.createTaskQuery().taskId(approveRejectVO.getTaskId()).singleResult();
            taskService.complete(task.getId(),paramsMap);
            if (approved) {
                //如果是同意，还需要继续走一步
                Task t = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
                taskService.complete(t.getId());
            }
            return RespBean.ok("操作成功");
        }catch (Exception e){
            e.printStackTrace();
        }
        return RespBean.error("操作失败");
    }

    public RespBean searchResult(String name) {
        List<HistoryInfo> historyInfos = new ArrayList<>();
        List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(name).finished().orderByProcessInstanceEndTime().desc().list();
        for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
            HistoryInfo historyInfo = new HistoryInfo();
            Date startTime = historicProcessInstance.getStartTime();
            Date endTime = historicProcessInstance.getEndTime();
            List<HistoricVariableInstance> historicVariableInstances = historyService.createHistoricVariableInstanceQuery()
                    .processInstanceId(historicProcessInstance.getId())
                    .list();
            for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
                String variableName = historicVariableInstance.getVariableName();
                Object value = historicVariableInstance.getValue();
                if ("reason".equals(variableName)) {
                    historyInfo.setReason((String) value);
                } else if ("days".equals(variableName)) {
                    historyInfo.setDays(Integer.parseInt(value.toString()));
                } else if ("approved".equals(variableName)) {
                    historyInfo.setStatus((Boolean) value);
                } else if ("name".equals(variableName)) {
                    historyInfo.setName((String) value);
                }
            }
            historyInfo.setStartTime(startTime);
            historyInfo.setEndTime(endTime);
            historyInfos.add(historyInfo);
        }
        return RespBean.ok("ok", historyInfos);
    }
}
