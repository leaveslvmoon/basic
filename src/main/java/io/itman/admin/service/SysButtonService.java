package io.itman.admin.service;


import io.itman.library.util.JsonUtil;
import io.itman.model.SysButton;

public interface SysButtonService {
    JsonUtil del(String ID);
    JsonUtil save(SysButton model);
}
