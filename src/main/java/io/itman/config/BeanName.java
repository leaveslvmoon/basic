package io.itman.config;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.annotation.Configuration;

/**
 * @date 2018/1/31.
 */
@Configuration
public class BeanName implements BeanNameAware {

  @Override
  public void setBeanName(String name) {
   // System.out.println("BeanNameAware-------->:"+name);
  }
}
