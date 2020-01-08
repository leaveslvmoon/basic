package io.itman.admin.direct;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.template.Directive;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;
import io.itman.model.SysMenu;
import io.itman.model.SysRolemenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ButtonDirective extends Directive {
    private int paraNum;




    // ExprList 代表指令参数表达式列表4.
//    public void setExprList(ExprList exprList) {
//        this.paraNum = exprList.length();
//        if (this.paraNum == 0) {
//            throw new ParseException("标签没传参", this.location);
//        }else{
//            this.roleId=Integer.parseInt(exprList.getExpr(0).toString());
//        }
//        // 在这里可以对 exprList 进行个性化控制6.
//        System.out.println("===>"+exprList.getExpr(0));
//    }

    @Override
    public void exec(Env env, Scope scope, Writer writer) {
        String menuUrl = scope.getData().get("menuUrl").toString();
        String roleId = scope.getData().get("roleId").toString();
        write(writer, "<div class=\"xbbutton-group\">\r\n");
        System.out.println("---" + menuUrl);
        System.out.println("---" + roleId);
        List<Map> menuButtonList = menuButtonList(menuUrl, Integer.parseInt(roleId));
        if (menuButtonList != null && menuButtonList.size() > 0) {
            String strButton = "";
            for (Map map : menuButtonList) {
                write(writer, " <div class=\"xbbutton\" onclick=\"openURL('" + map.get("methodName") + "')\"><i class=\"fa " + map.get("icon") + "\"></i>" + map.get("name") + "</div>\r\n");
            }
            //stat.exec(env, scope, writer);
            write(writer, " </div>");
            write(writer, " <script>\r\n");
            write(writer, " function openURL(type) { \r\n");
            write(writer, " switch (type) { \r\n");
            String menUrl = menuButtonList.get(0).get("menUrl").toString();
            for (Map map : menuButtonList) {
                StringBuilder str = new StringBuilder(" case '" + map.get("methodName") + "':\r\nXB.open({\r\n'type': '" + map.get("methodName") + "',\r\n'openmode': '" + map.get("openMode") + "',\r\n'dialog': {'url': '");
                if ("".equals(map.get("buttonUrl").toString())) {
                    String buttonUrl = menUrl.substring(menUrl.indexOf("/"), menUrl.lastIndexOf("/") + 1);
                    if ("add".equals(map.get("methodName").toString()) || "edit".equals(map.get("methodName").toString())) {
                        buttonUrl = buttonUrl + "edit/";
                    } else {
                        buttonUrl = buttonUrl + map.get("methodName").toString()+"/";
                    }
                    str.append(buttonUrl+"'");
                } else {
                    str.append(map.get("buttonUrl")+"'");
                }
                if (map.get("width") != null) {
                    str.append(", 'width': '" + map.get("width") + "'");
                }
                if (map.get("height") != null) {
                    str.append(", 'height': '" + map.get("height") + "'");
                }
                str.append(", 'title': '" + map.get("name") + "'");
                if (!("".equals(map.get("buttonSaveUrl").toString()))) {
                    str.append(", 'save': {'url': '" + map.get("buttonSaveUrl") + "'}");
                }
                str.append("}\r\n});\r\n break; \r\n");
//            write(writer," case '"+map.get("methodName")+"':XB.open({'type': '"+map.get("methodName")+"','openmode': '"+map.get("openMode")+"','dialog': {'url': '"+map.get("buttonUrl")+"', 'title': '"+map.get("name")+"', 'width': 650, 'height': 400}}); break; \r\n");
                write(writer, str.toString());
            }

            write(writer, "  } \r\n");
            write(writer, "  } \r\n");
            write(writer, " </script>");
            //生成反选 div
            String div = "<div id=\"datagridMenu\" class=\"easyui-menu\" style=\"display:none;\">\r\n";
            String str = "";
            for (Map map : menuButtonList) {
                str = str+"<div data-options=\"name:'"+map.get("methodName")+"'\">"+map.get("name")+"</div>\r\n";
            }
            String divStr = div+str+"    <div class=\"menu-sep\"></div>\r\n" +
                    "    <div data-options=\"name:'all'\">全选</div>\r\n" +
                    "    <div data-options=\"name:'clearall'\">全不选</div>\r\n" +
                    "    <div data-options=\"name:'anti'\">反选</div>\r\n" +
                    "    <div class=\"menu-sep\"></div>\r\n"+"</div>";
            write(writer,divStr);
        }

    }

//    public boolean hasEnd() {
//        return true;  // 返回 true 则该指令拥有 #end 结束标记
//    }

    public List<Map> menuButtonList(String menuUrl, int roleId){
        SysMenu sysmenu = SysMenu.dao.findFirst("select id from sys_menu where url = '" + menuUrl + "'");
        List<Map> menuButtonList = new ArrayList<>();
        int menuId = sysmenu.getId();
        String buttonIds = SysRolemenu.dao.findFirst("select buttonId from sys_rolemenu where menuId = '" + menuId + "' and roleId = '" + roleId + "'").getButtonId();
        if(!(buttonIds==null|| "".equals(buttonIds))){
            List<Integer> buttonIdList = Arrays.asList(buttonIds.split(",")).stream().map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
            for (int buttonId : buttonIdList) {
                Record r = Db.findFirst("select (select url from sys_menu where id = "+menuId+" )menUrl,sb.name,sb.methodName,sb.icon,smb.buttonUrl,smb.buttonSaveUrl,smb.width,smb.height,smb.openMode,smb.jsFunctionBefore \n" +
                        "from sys_button sb LEFT JOIN sys_menubutton smb on smb.buttonId=sb.id  where menuId ='" + menuId + "' and buttonId = '" + buttonId + "'");
                if(r!=null){
                    menuButtonList.add(r.getColumns());
                }
            }
        }
        return menuButtonList;
    }



}
