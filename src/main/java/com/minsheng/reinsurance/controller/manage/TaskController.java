package com.minsheng.reinsurance.controller.manage;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.minsheng.reinsurance.bean.entity.TaskInfo;
import com.minsheng.reinsurance.controller.BaseController;
import com.minsheng.reinsurance.enums.DeleteFlagEnum;
import com.minsheng.reinsurance.service.TaskService;
import com.minsheng.reinsurance.utils.ObjMapConvertUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 *
 */
@Controller
@RequestMapping("${adminPath}/manage/task")
public class TaskController extends BaseController {
    @Autowired
    TaskService service;



    private final static String TEMPLATEPRE = "/manage/task/";

    //    @RequiresRoles("超级管理员")
    @RequiresPermissions("sysAdministrative:task:index")
    @RequestMapping("/index")
    public String index() {
        return getViews(TEMPLATEPRE, "index");
    }


    @RequiresPermissions("sysAdministrative:task:add")
    @ResponseBody
    @RequestMapping("/add")
    public Object add(TaskInfo taskInfo){
        HashMap<String, Object> rtn = new HashMap<>();
        if(service.validateTaskParams(rtn,taskInfo,"add")){

            taskInfo.setJobGroup("task");
            taskInfo.setCreateTime(new Date().toString());
            taskInfo.setDelFlag(1);
            int i = service.insert(taskInfo);
            //状态为启动时，启动定时任务
            if(taskInfo.getJobStatus().equals("1")){
                service.addJob(rtn,taskInfo);
            }

            return true;
        }

        return rtn;
    }


    @RequiresPermissions("sysAdministrative:task:index")
    @RequestMapping("/get_tasks_json")
    @ResponseBody
    public Object getRolesJson(Integer currentPage, Integer pageSize, TaskInfo taskInfo) throws Exception {
        HashMap<String, Object> result = new HashMap<>();
        HashMap<String, Object> params = new HashMap<>();
//        params = Conversion.objToMap(params, role, true);
        params.put("delFlag", DeleteFlagEnum.NORMAL.getCode());
        PageHelper.startPage(currentPage,pageSize);
        List<TaskInfo> list = service.findBy(params);
        List<Map<String, Object>> lists = new ArrayList<>();
        for (TaskInfo item : list) {
            //List<Integer> menuIds = service.getMenuIdsByRoleId(item.getId());
            //item.setMenuIds(menuIds);
            lists.add(ObjMapConvertUtil.objectToMap(item));
        }
        PageInfo pageInfo = new PageInfo(list);

        result.put("count", pageInfo.getTotal());
       // System.out.println(lists);
        result.put("lists", lists);
        return result;
    }

    @RequiresPermissions("sysAdministrative:task:changeStatus")
    @RequestMapping("/changeStatus")
    @ResponseBody
    public Object changeStatus(Integer id, String jobStatus) {
        TaskInfo taskInfo = service.getEntityById(id);
        HashMap<String, Object> rtn = new HashMap<>();
        if (taskInfo != null) {
            taskInfo.setJobStatus(jobStatus);
            if(jobStatus.equals("1")){
                service.addJob(rtn,taskInfo);
            }else {
                service.deleteJob(taskInfo.getJobName(),taskInfo.getJobGroup());
            }
            service.update(taskInfo);
            return true;
        }
        return false;
    }

    @RequiresPermissions("sysAdministrative:task:edit")
    @RequestMapping("/update")
    @ResponseBody
    public Object update(TaskInfo taskInfo) {
        HashMap<String, Object> rtn = new HashMap<>();

        if (service.validateTaskParams(rtn, taskInfo,"upate")) {
            taskInfo.setJobGroup("task");
            taskInfo.setCreateTime(new Date().toString());
            taskInfo.setDelFlag(1);
            service.update(taskInfo);
            //如果状态为启动，则修改定时任务
            if(taskInfo.getJobStatus().equals("1")){
                service.editJob(taskInfo);
            }
            return true;
        }
        return rtn;
    }

    @RequiresPermissions("sysAdministrative:task:del")
    @RequestMapping("/del")
    @ResponseBody
    public Object delete(Integer id) {
        HashMap<String, Object> rtn = new HashMap<>();
        if (id == null || id.equals(0)) {
            return false;
        } else {
            TaskInfo taskInfo = service.getEntityById(id);
            if (taskInfo != null) {
                if(taskInfo.getJobStatus().equals("1")){
                    rtn.put("msg","当前定时任务为启动状态，请先修改任务状态，再操作删除操作");

                }else{
                    taskInfo.setDelFlag(DeleteFlagEnum.DELETED.getCode());
                    service.update(taskInfo);
                    return true;
                }

            }else{
                rtn.put("msg","未接收到当前任务信息");
            }
            return rtn;
        }
    }

    /**
     * 启动服务，启动所有状态jobStatus为1的任务
     * @return
     */
    @RequiresPermissions("sysAdministrative:task:add")
    @ResponseBody
    @RequestMapping("/addAllJob")
    public Object add(){

        HashMap<String, Object> rtn = new HashMap<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("delFlag", DeleteFlagEnum.NORMAL.getCode());
        params.put("jobStatus","1");
        List<TaskInfo> list = service.findBy(params);
        for (TaskInfo item : list) {
            service.addJob(rtn,item);
        }
        return true;
    }
}
