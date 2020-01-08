package io.itman.admin.service;


import io.itman.library.util.JsonUtil;
import io.itman.model.MacWhitelist;

public interface MacWhitelistService {
    JsonUtil save(MacWhitelist model);
}
