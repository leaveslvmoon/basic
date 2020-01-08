package io.itman.admin.service;



import io.itman.library.util.JsonUtil;
import io.itman.library.vo.Tree;
import io.itman.model.ApiContent;

import java.util.List;

public interface ApiContentService {
    public List<Tree> buildTree();
    JsonUtil save(ApiContent model);
}
