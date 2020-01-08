package io.itman.admin.service.impl;


import io.itman.admin.service.SysButtonService;
import io.itman.library.util.JsonUtil;
import io.itman.library.util.StringUtil;
import io.itman.model.SysButton;
import io.itman.model.SysMenubutton;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysButtonServiceImpl implements SysButtonService {
    /*
     * @Author CES
     * @Description //判断按钮名称重复
     * @Param [name]
     * @return int
     **/
    public int name(String name) {
        List<SysButton> buttonList = SysButton.dao.find("SELECT * FROM sys_button where name ='"+name+"'");
        if(buttonList.size()>0){
            return 0;
        }
        return 1;
    }

    public int name(String name, int id) {
        List<SysButton> buttonList = SysButton.dao.find("SELECT * FROM sys_button where name ='"+name+"' and id != '"+id+"'");
        if(buttonList.size()>0){
            return 0;
        }
        return 1;
    }

    /*
     * @Author CES
     * @Description //判断按钮方法名称重复
     * @Param [methodName]
     * @return int
     **/
    public int methodName(String methodName) {
        List<SysButton> buttonList = SysButton.dao.find("SELECT * FROM sys_button where methodName ='"+methodName+"'");
        if(buttonList.size()>0){
            return 0;
        }
        return 1;
    }

    public int methodName(String methodName, int id) {
        List<SysButton> buttonList = SysButton.dao.find("SELECT * FROM sys_button where methodName ='"+methodName+"' and id != '"+id+"'");
        if(buttonList.size()>0){
            return 0;
        }
        return 1;
    }

    @Override
    public JsonUtil del(String ID) {
        JsonUtil result = new JsonUtil();
        for (String id : ID.split(",")) {
            List<SysMenubutton> sysMenubuttonList = SysMenubutton.dao.find("select * from sys_menubutton where buttonId = " + id + "");
            if(sysMenubuttonList.size()>0){
                result.setMessage("该按钮已绑定菜单，禁止删除");
                result.setResult(0);
                return result;
            }
            SysButton.dao.deleteById(id);
        }
        result.setMessage("删除成功");
        return result;
    }


    @Override
    public JsonUtil save(SysButton model) {
        JsonUtil result = new JsonUtil();
        if (model.getSort() == null) {
            model.setSort(StringUtil.DEFAULT_SORT);
        }
        if (model.getId() == 0) {
            int name = this.name(model.getName());
            if(name==0){
                result.setResult(0);
                result.setMessage("按钮名称重复");
                return result;
            }
            int methodName = this.methodName(model.getMethodName());
            if(methodName==0){
                result.setResult(0);
                result.setMessage("按钮方法名称重复");
                return result;
            }
            model.save();
            result.setMessage("恭喜您，添加成功！");
        } else {
            int name = this.name(model.getName(),model.getId());
            if(name==0){
                result.setResult(0);
                result.setMessage("按钮名称重复");
                return result;
            }
            int methodName = this.methodName(model.getMethodName(),model.getId());
            if(methodName==0){
                result.setResult(0);
                result.setMessage("按钮方法名称重复");
                return result;
            }
            model.update();
            result.setMessage("恭喜您，修改成功！");
        }
        return result;
    }

}
