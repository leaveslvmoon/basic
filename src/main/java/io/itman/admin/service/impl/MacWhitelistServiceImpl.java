package io.itman.admin.service.impl;


import io.itman.admin.service.MacWhitelistService;
import io.itman.library.util.JsonUtil;
import io.itman.model.MacWhitelist;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MacWhitelistServiceImpl implements MacWhitelistService {
    /*
     * @Author CES
     * @Description //判断mac名称重复
     * @Param [name]
     * @return int
     **/
    public int name(String name) {
        List<MacWhitelist> buttonList = MacWhitelist.dao.find("SELECT * FROM mac_whitelist where mac ='"+name+"'");
        if(buttonList.size()>0){
            return 0;
        }
        return 1;
    }

    public int name(String name, int id) {
        List<MacWhitelist> buttonList = MacWhitelist.dao.find("SELECT * FROM mac_whitelist where mac ='"+name+"' and id != '"+id+"'");
        if(buttonList.size()>0){
            return 0;
        }
        return 1;
    }

    @Override
    public JsonUtil save(MacWhitelist model) {
        JsonUtil result = new JsonUtil();
        if (model.getId() == 0) {
            int name = this.name(model.getMac());
            if(name==0){
                result.setResult(0);
                result.setMessage("mac地址重复");
                return result;
            }
            model.save();
            result.setMessage("恭喜您，添加成功！");
        } else {
            int name = this.name(model.getMac(),model.getId());
            if(name==0){
                result.setResult(0);
                result.setMessage("mac地址重复");
                return result;
            }
            model.update();
            result.setMessage("恭喜您，修改成功！");
        }
        return result;
    }

}
