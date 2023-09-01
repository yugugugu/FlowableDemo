package yugugugu.flowable.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import yugugugu.flowable.service.AskForLeaveService;
import yugugugu.flowable.utils.RespBean;
import yugugugu.flowable.vo.ApproveRejectVO;
import yugugugu.flowable.vo.AskFroLeaveVO;

/**
 * @author liuyu
 * @date 2023/8/27
 **/

@RequestMapping("askForLeave")
@RestController
public class AskForLeaveController {
    @Autowired
    AskForLeaveService askForLeaveService;

    @GetMapping("/hello")
    public RespBean hello(){
        return RespBean.ok("hello");
    }


    /**
     * 申请假期
     * @param leaveVo
     * @return
     */
    @PostMapping("/begin")
    public RespBean askForLeave(@RequestBody AskFroLeaveVO leaveVo){
        return askForLeaveService.askForLeave(leaveVo);
    }

    /**
     * 申请任务列表
     * @param identity
     * @return
     */
    @GetMapping("/list")
    public RespBean leaveList(String identity) {
        return askForLeaveService.leaveList(identity);
    }

    /**
     * 请假审批
     * @param approveRejectVO
     * @return
     */
    @PostMapping("/handler")
    public RespBean askForLeaveHandler(@RequestBody ApproveRejectVO approveRejectVO) {
        return askForLeaveService.askForLeaveHandler(approveRejectVO);
    }

    /**
     * 结果查询
     * @param name
     * @return
     */
    @GetMapping("/search")
    public RespBean searchResult(String name) {
        return askForLeaveService.searchResult(name);
    }
}
