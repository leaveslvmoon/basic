package io.itman.admin.service.impl;


import io.itman.admin.service.SMSService;
import io.itman.admin.vo.User;
import io.itman.library.util.JsonUtil;
import io.itman.library.util.StringUtil;
import io.itman.model.SysSmstemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Describe
 *
 * @author Jxx
 * @date 2018/12/11
 */
@Service
public class SMSServiceImpl implements SMSService {

    @Override
    public JsonUtil save(HttpServletRequest request, SysSmstemplate smstemplate) {
        if (StringUtil.isEmptyString( smstemplate.getContents())){
            return JsonUtil.error("请输入模板内容");
        }
        User user = (User) request.getSession().getAttribute("user");
        smstemplate.setCreateBy(user.getAdministrator().getId());

        if (smstemplate.getSort()==null){
            smstemplate.setSort(StringUtil.DEFAULT_SORT);
        }
        smstemplate.setCreateDate(new Date());
        JsonUtil result = new JsonUtil();
        if (smstemplate.getId() == 0) {
            smstemplate.save();
            return   JsonUtil.sucess("恭喜您，添加成功！");
        } else {
            smstemplate.update();
            return   JsonUtil.sucess("恭喜您，修改成功！");
        }
    }

    @Override
    public JsonUtil del(String ID) {
        JsonUtil result = new JsonUtil();
        if (!StringUtil.isEmptyString(ID)){
            for (String s : ID.split(StringUtil.DEFAULT_SPLIT)) {
                SysSmstemplate sysSmstemplate = SysSmstemplate.dao.findById(s);
                if (sysSmstemplate!=null){
                    sysSmstemplate.setStatus(0);
                    sysSmstemplate.update();
                }
            }

        }
        result.setMessage("删除成功");
        return result;
    }
}
