package io.itman.admin.service.impl;


import io.itman.admin.service.AdministratorService;
import io.itman.admin.vo.User;
import io.itman.library.util.JsonUtil;
import io.itman.library.util.MD5;
import io.itman.library.util.RequestUtil;
import io.itman.model.SysAdministrator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministratorServiceImpl implements AdministratorService {

    /**
     * @Author ces
     * @Description 新增用户名不重复
     * @Param [userName]
     * @return int
     **/
    public int number(String userName) {
        List<SysAdministrator> administratorList = SysAdministrator.dao.find("SELECT * FROM sys_administrator where userName ='" + userName + "' and isDelete = 0");
        if (administratorList.size() > 0) {
            return 0;
        }
        return 1;
    }

    /**
     * @Author ces
     * @Description 修改用户名不重复
     * @Param [userName]
     * @return int
     **/
    public int number(int id, String userName) {
        List<SysAdministrator> administratorList = SysAdministrator.dao.find("SELECT * FROM sys_administrator where userName ='" + userName + "' and id != '" + id + " ' and isDelete=0");
        if (administratorList.size() > 0) {
            return 0;
        }
        return 1;
    }

    /**
     * @Author ces
     * @Description 新增修改保存动作
     * @Param [model]
     * @return cc.ahxb.util.JsonUtil
     **/
    @Override
    public JsonUtil save(SysAdministrator model) {
        JsonUtil result = new JsonUtil();
        model.setIsDelete(0);
        if (model.getId() == 0) {
            int number = this.number(model.getUserName());
            if (number == 0) {
                result.setResult(0);
                result.setMessage("用户名重复");
                return result;
            }
            model.setUserPsd(MD5.getMD5(model.getUserPsd()));
            model.setPsdErrorCount(0);
            model.setLoginCount(0);
            User user = (User) RequestUtil.getSession().getAttribute("user");
            model.setCreateBy(user.getAdministrator().getId());
            model.save();
            result.setMessage("恭喜您，添加成功！");
        } else {
            int number = this.number(model.getId(), model.getUserName());
            if (number == 0) {
                result.setResult(0);
                result.setMessage("用户名重复");
                return result;
            }
            if(!("******".equals(model.getUserPsd()))){
                model.setUserPsd(MD5.getMD5(model.getUserPsd()));
            }else {
                String usrPsd = SysAdministrator.dao.findById(model.getId()).getUserPsd();
                model.setUserPsd(usrPsd);
            }
            model.update();
            result.setMessage("恭喜您，修改成功！");
        }
        return result;
    }
}
