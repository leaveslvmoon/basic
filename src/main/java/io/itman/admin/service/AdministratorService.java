package io.itman.admin.service;


import io.itman.library.util.JsonUtil;
import io.itman.model.SysAdministrator;

public interface AdministratorService {
    JsonUtil save(SysAdministrator model);
}
