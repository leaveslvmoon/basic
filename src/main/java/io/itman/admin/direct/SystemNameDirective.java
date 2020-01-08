package io.itman.admin.direct;

import com.jfinal.template.Directive;
import com.jfinal.template.Env;
import com.jfinal.template.TemplateException;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;
import io.itman.model.SysBasicinfo;

import java.io.IOException;

public class SystemNameDirective extends Directive {
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
    String systemName;
    {
        systemName = SysBasicinfo.dao.findFirst("select * from sys_basicinfo").getSystemName();
    }

    @Override
    public void exec(Env env, Scope scope, Writer writer) {
        try {
            writer.write(systemName);
        } catch (IOException var6) {
            throw new TemplateException(var6.getMessage(), this.location, var6);
        }

    }
}
