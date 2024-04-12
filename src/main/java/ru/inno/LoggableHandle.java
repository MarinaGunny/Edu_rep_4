package ru.inno;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Calendar;

@Component
public class LoggableHandle implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);

    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (!bean.getClass().isAnnotationPresent(Loggable.class)) return bean;

        //Делаем объект на основе нашего бина, невешиваем на него полезные штуки и возвращаем не наш бин, а этот объект
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(bean.getClass());
        enhancer.setCallback(new MethodInterceptor() {
            @Override

            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                Class cls = bean.getClass();
                String path;
                Object res;

                res = method.invoke(bean, args);
                Annotation a =cls.getDeclaredAnnotation(Loggable.class);
                path = ((Loggable)a).logpath();
                //Пишем в лог
                FileWriter fw;
                try {
                    fw = new FileWriter(path);
                    fw.write("Date:" + Calendar.getInstance().getTime().toString()+ " Method: " + method.getName()
                            + " Input: "+ Arrays.stream(args).toList().toString()
                            + "Output: " + res.toString());
                    fw.close();
                } catch (IOException ioe) {
                    throw new RuntimeException(ioe);
                }
                return res;
            }
        });

        Constructor cons= bean.getClass().getConstructors()[0];

        Object [] arr=new Object[cons.getParameterCount()];

        return enhancer.create(cons.getParameterTypes(),arr);
    }
}
