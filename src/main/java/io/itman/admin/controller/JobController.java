package io.itman.admin.controller;


import io.itman.admin.core.quartz.JobTask;
import io.itman.admin.filter.DateValueFilter;
import io.itman.admin.filter.StatusValueFilter;
import io.itman.admin.vo.User;
import io.itman.library.util.*;
import io.itman.model.SysJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * @date 2018/1/6.
 * <p>
 * 定时任务 controller
 */
@Controller
@RequestMapping("/admin/job")
public class JobController {

    String parentUrl = "/job/";
//    @Autowired
//    JobService jobService;

    @Autowired
    JobTask jobTask;

    @GetMapping(value = "/showCron")
    public String showCron(String cron,HttpServletRequest request) {
        request.setAttribute("cron", cron);
        return parentUrl + "cron";
    }

    @GetMapping(value = "/index")
    public String showUser(Model model) {
        return parentUrl + "index";
    }

    @ResponseBody
    @PostMapping("/DataList")
    public String DataList(int page, int rows, @RequestParam Map<String, Object> map) {
        DataGridReturn datalist = CommonUtil.DataGridData(page, rows, "getJobList", map);
        return JsonObjectUtils.objectToJson(datalist,new StatusValueFilter(),new DateValueFilter("createDate"));
    }

    @GetMapping(value = "/edit")
    public String edit(@RequestParam(value = "ID", defaultValue = "0") int ID, HttpServletRequest request) {
        request.setAttribute("id", ID);
        if(ID!=0){
            request.setAttribute("cron", SysJob.dao.findById(ID).getCron());
        }
        return (parentUrl + "edit");
    }

    @GetMapping(value = "/Show")
    @ResponseBody
    public SysJob Show(@RequestParam(value = "id", defaultValue = "0") String id) {
        SysJob model = SysJob.dao.findById(id);

        return model;
    }


    @PostMapping(value = "/save")
    @ResponseBody
    public JsonUtil save(SysJob model) {
        JsonUtil result = new JsonUtil();
        if (model.getId() == 0) {
            User user = (User) RequestUtil.getSession().getAttribute("user");
            int createId = user.getAdministrator().getId();
            model.setCreateBy(createId);
            model.setCreateDate(new Date());
            model.setStatus(false);
            model.save();
            result.setMessage("恭喜您，添加成功！");
        } else {
            if (jobTask.checkJob(model)) {
                result.setMessage("已经启动任务无法更新,请停止后更新");
                result.setResult(1);
                return result;
            }
            User user = (User) RequestUtil.getSession().getAttribute("user");
            int updateId = user.getAdministrator().getId();
            model.setUpdateBy(updateId);
            model.setUpdateDate(new Date());
            model.update();
            result.setMessage("恭喜您，修改成功！");
        }
        return result;
    }


    @PostMapping(value = "del")
    @ResponseBody
    public JsonUtil del(String ID) {
        JsonUtil result = new JsonUtil();
        for (String id : ID.split(",")) {
            SysJob job = SysJob.dao.findById(id);
            boolean flag = jobTask.checkJob(job);
            if ((flag && !job.getStatus()) || !flag && job.getStatus()) {
                result.setMessage("您任务表状态和web任务状态不一致,无法删除");
                return result;
            }
            if (flag) {
                result.setMessage("该任务处于启动中，无法删除");
                return result;
            }
            SysJob.dao.deleteById(id);
        }
        result.setMessage("删除成功");
        return result;
    }


    //    @Log(desc = "启动任务")
    @PostMapping(value = "startJob")
    @ResponseBody
    public JsonUtil startJob(String ID) {
        JsonUtil result = new JsonUtil();
        SysJob job = SysJob.dao.findById(ID);
        jobTask.startJob(job);
        job.setStatus(true);
        job.update();
        result.setMessage("启动成功");
        return result;
    }

    //    @Log(desc = "停止任务")
    @PostMapping(value = "endJob")
    @ResponseBody
    public JsonUtil endJob(String ID) {
        JsonUtil result = new JsonUtil();
        String msg = null;
        SysJob job = SysJob.dao.findById(ID);
        jobTask.remove(job);
        job.setStatus(false);
        job.update();
        result.setMessage("停止成功");
        return result;
    }

}
