package com.mojiayi.action.javabasis.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author mojiayi
 */
public class MojiayiProxy {
    public static void main(String[] args) {
        myInstance();
    }

    private static void myInstance() {
        Collection proxy = (Collection) Proxy.newProxyInstance(Collection.class.getClassLoader(), new Class[]{Collection.class}, new InvocationHandler() {
            final List<?> target = new ArrayList<>();
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                long startTime = System.currentTimeMillis();
                Thread.sleep(1000L);
                Object value = method.invoke(target, args);
                long finishTime = System.currentTimeMillis();
                System.out.println("方法" + method.getName() + "耗时=" + (finishTime - startTime) + "ms");
                return value;
            }
        });

        proxy.add("a");
        proxy.add("b");
        proxy.add("c");
        proxy.add("d");
        proxy.remove("b");
        proxy.size();
    }
}
