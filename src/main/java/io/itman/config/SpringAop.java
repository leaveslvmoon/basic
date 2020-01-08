package io.itman.config;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Aspect//描述一个切面类，定义切面类的时候需要打上这个注解
@Component
@Slf4j
public class SpringAop {
    @Before("@annotation(txService)")// 拦截被TestAnnotation注解的方法；如果你需要拦截指定package指定规则名称的方法，可以使用表达式execution(...)，具体百度一下资料一大堆
    public void beforeTest(JoinPoint point, TxService txService) throws Throwable {
        System.out.println("beforeTest:" );

    }

    @Around("@annotation(txService)")
    public void huanrao(ProceedingJoinPoint poin,TxService txService){

            Db.tx(new IAtom(){
                @Override
                public boolean run() throws SQLException {
                    try {
                        poin.proceed();
                        return true;
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                        return false;

                    }

              }});




    }


    @After("@annotation(txService)")
    public void afterTest(JoinPoint point, TxService txService) {
        System.out.println("afterTest:" );
    }

}