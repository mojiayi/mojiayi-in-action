package com.mojiayi.action.javabasis.reflection;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mojiayi
 */
public class MojiayiReflectionTest {
    @Test
    public void testBasicReflectionFeature() throws ClassNotFoundException {
        String classFullName = "com.mojiayi.action.javabasis.reflection.DemoGitConfig";
        Class<?> clazz = Class.forName(classFullName);

        Assert.assertEquals(classFullName, clazz.getCanonicalName());

        Field[] onlyPublicFields = clazz.getFields();
        Assert.assertEquals(1, onlyPublicFields.length);
        Assert.assertEquals("phrase", onlyPublicFields[0].getName());

        Field[] allFields = clazz.getDeclaredFields();
        Assert.assertEquals(3, allFields.length);
        List<String> fieldNames = Arrays.stream(allFields).map(Field::getName).sorted().collect(Collectors.toList());
        Assert.assertEquals("email", fieldNames.get(0));
        Assert.assertEquals("phrase", fieldNames.get(1));
        Assert.assertEquals("username", fieldNames.get(2));

        Constructor<?>[] onlyPublicConstructors = clazz.getConstructors();
        Assert.assertEquals(2, onlyPublicConstructors.length);

        Constructor<?>[] allConstructors = clazz.getDeclaredConstructors();
        Assert.assertEquals(3, allConstructors.length);

        Method[] onlyPublicMethods = clazz.getMethods();
        Assert.assertEquals(13, onlyPublicMethods.length);

        Method[] allMethods = clazz.getDeclaredMethods();
        Assert.assertEquals(6, allMethods.length);
    }
}
