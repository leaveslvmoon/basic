package io.itman.admin.service;



import io.itman.admin.vo.RoleMenu;
import io.itman.library.util.JsonUtil;

import javax.servlet.http.HttpServletRequest;

public interface RoleService {
    JsonUtil result(int roleId, RoleMenu roleMenu);
    void goPermissions(String ID, HttpServletRequest request);
    JsonUtil del(String ID);
}
