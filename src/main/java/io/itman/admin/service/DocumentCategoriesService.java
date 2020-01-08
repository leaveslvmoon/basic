package io.itman.admin.service;



import io.itman.library.util.DataGridReturn;
import io.itman.library.util.JsonUtil;
import io.itman.model.DocumentCategories;

import java.util.Map;

public interface DocumentCategoriesService {
    DataGridReturn DataList(Map<String, Object> map);
    JsonUtil save(DocumentCategories model);
    JsonUtil del(String ID);
}
