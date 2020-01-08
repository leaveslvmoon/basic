package io.itman.admin.service;



import io.itman.library.util.JsonUtil;
import io.itman.library.vo.Tree;
import io.itman.model.DocumentContent;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface DocumentContentService {
    public List<Tree> buildTree();
    JsonUtil del(String ID);
    JsonUtil save(HttpServletRequest request, DocumentContent model);
}
