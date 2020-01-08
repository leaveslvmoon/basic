package io.itman.admin.service;



import io.itman.admin.vo.SysmenubuttonList;
import io.itman.library.util.DataGridReturn;
import io.itman.library.util.JsonUtil;
import io.itman.library.vo.Tree;
import io.itman.model.SysMenu;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface MenuService {
    public List<Tree> buildTree();
    JsonUtil del(String ID);
    void edit(int ID, HttpServletRequest request);
    JsonUtil save(SysMenu model, SysmenubuttonList sysmenubuttonList);
    void MenuButton(String id, HttpServletRequest request);
    List<Object> getMenuTree();
    DataGridReturn DataList(Map<String, Object> map);
}
