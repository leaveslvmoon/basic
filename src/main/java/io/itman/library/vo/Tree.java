package io.itman.library.vo;

/**
 * 类名称：Tree
 * 类描述：树形结构
 */
public class Tree {

    private Integer id;
    private Integer pId;
    private String name;
    private String iconCls;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }


    @Override
    public String toString() {
        return "Tree [id=" + id + ", pId=" + pId + ", name=" + name + "]";
    }

}