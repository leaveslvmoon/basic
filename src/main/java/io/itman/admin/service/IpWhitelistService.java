package io.itman.admin.service;


import io.itman.library.util.JsonUtil;
import io.itman.model.IpWhitelist;

public interface IpWhitelistService {
    JsonUtil save(IpWhitelist model);
}
