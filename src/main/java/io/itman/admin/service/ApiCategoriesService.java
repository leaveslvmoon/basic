package io.itman.admin.service;



import io.itman.library.util.DataGridReturn;
import io.itman.library.util.JsonUtil;
import io.itman.model.ApiCategories;

import java.util.Map;

public interface ApiCategoriesService {
    DataGridReturn DataList(Map<String, Object> map);
    JsonUtil save(ApiCategories model);
    JsonUtil del(String ID);
}
