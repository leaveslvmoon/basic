package io.itman.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.annotation.Configuration;

/**
 * @date 2018/1/31.
 */
@Configuration
public class BeanFactoryImpl implements BeanFactoryAware {


  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    //System.out.println("BeanFactoryAware------->"+beanFactory);
  }
}
