package io.itman.admin.core.quartz.CustomQuartz;

//import cc.ahxb.entity.SysUser;
//import cc.ahxb.service.SysUserService;
//import cc.ahxb.util.SpringUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;

/**
 * @date 2018/1/7.
 *
 * 定时
 */
public class JobDemo1 implements Job {

//  @Autowired
//  SysUserService sys;

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    System.out.println("JobDemo1：启动任务=======================");
    run();
    System.out.println("JobDemo1：下次执行时间====="+
        new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            .format(context.getNextFireTime())+"==============");
  }

  public void run(){
//    ApplicationContext applicationContext=SpringUtil.getApplicationContext();
    //可以 获取
    //SysUserService sys=SpringUtil.getBean(SysUserServiceImpl.class);
//    List<SysUser> userList=sys.selectListByPage(new SysUser());
//    System.out.println(userList.get(0).getUsername());;

    System.out.println("JobDemo1：执行完毕=======================");

  }
}
