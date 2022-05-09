package com.mojiayi.action.javabasis.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

/**
 * @author mojiayi
 */
public class PrintClassInfoByProxy {
    public static void main(String[] args) {
        getConstructor(List.class);
        getConstructor(Map.class);
    }

    private static void getConstructor(Class<?> clazz) {
        Class<?> proxy = Proxy.getProxyClass(clazz.getClassLoader(), clazz);
        Constructor<?>[] constructors = proxy.getConstructors();
        StringBuilder stringBuilder = new StringBuilder();
        System.out.println("constructor list:");
        int index = 1;
        for (Constructor<?> constructor : constructors) {
            if (stringBuilder.length() > 0) {
                stringBuilder.delete(0, stringBuilder.capacity());
            }
            Class<?>[] params = constructor.getParameterTypes();
            for (Class<?> param : params) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(param.getName());
            }
            stringBuilder.append(")");
            stringBuilder.insert(0, "(");
            stringBuilder.insert(0, constructor.getName());
            System.out.println(index + ". " + stringBuilder);
            index++;
        }
        index = 1;
        System.out.println("method list:");
        Method[] methods = proxy.getMethods();
        for (Method method : methods) {
            if (stringBuilder.length() > 0) {
                stringBuilder.delete(0, stringBuilder.capacity());
            }
            Class<?>[] params = method.getParameterTypes();
            for (Class<?> param : params) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(param.getName());
            }
            stringBuilder.append(")");
            stringBuilder.insert(0, "(");
            stringBuilder.insert(0, method.getName());
            stringBuilder.insert(0, " ");
            Class<?> returnType = method.getReturnType();
            stringBuilder.insert(0, returnType.getName());
            System.out.println(index + ". " + stringBuilder);
            index++;
        }
        System.out.println(">>>>>>");
    }
}
