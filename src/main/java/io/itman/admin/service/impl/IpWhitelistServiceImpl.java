package io.itman.admin.service.impl;


import io.itman.admin.service.IpWhitelistService;
import io.itman.library.util.JsonUtil;
import io.itman.model.IpWhitelist;
import io.itman.model.SysButton;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IpWhitelistServiceImpl implements IpWhitelistService {
    /*
     * @Author CES
     * @Description //判断ip名称重复
     * @Param [name]
     * @return int
     **/
    public int name(String name) {
        List<SysButton> buttonList = SysButton.dao.find("SELECT * FROM ip_whitelist where ip ='"+name+"'");
        if(buttonList.size()>0){
            return 0;
        }
        return 1;
    }

    public int name(String name, int id) {
        List<SysButton> buttonList = SysButton.dao.find("SELECT * FROM ip_whitelist where ip ='"+name+"' and id != '"+id+"'");
        if(buttonList.size()>0){
            return 0;
        }
        return 1;
    }

    @Override
    public JsonUtil save(IpWhitelist model) {
        JsonUtil result = new JsonUtil();
        if (model.getId() == 0) {
            int name = this.name(model.getIp());
            if(name==0){
                result.setResult(0);
                result.setMessage("ip重复");
                return result;
            }
            model.save();
            result.setMessage("恭喜您，添加成功！");
        } else {
            int name = this.name(model.getIp(),model.getId());
            if(name==0){
                result.setResult(0);
                result.setMessage("ip重复");
                return result;
            }
            model.update();
            result.setMessage("恭喜您，修改成功！");
        }
        return result;
    }

}
