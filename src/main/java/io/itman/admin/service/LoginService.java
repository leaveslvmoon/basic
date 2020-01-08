package io.itman.admin.service;


import io.itman.library.util.JsonUtil;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {
    String login(HttpServletRequest request);
    void index(HttpServletRequest request);
    JsonUtil save(HttpServletRequest request, String password, String newPwd, String confirmPwd);
}
