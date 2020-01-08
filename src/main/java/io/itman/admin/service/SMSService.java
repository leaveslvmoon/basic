package io.itman.admin.service;



import io.itman.library.util.JsonUtil;
import io.itman.model.SysSmstemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * Describe
 *
 * @author Jxx
 * @date 2018/12/11
 */
public interface SMSService {
    JsonUtil save(HttpServletRequest request, SysSmstemplate smstemplate);
    JsonUtil del(String ID);
}
