package com.mycompany.cloud.controller.test.java.classloader;

/**
 * @author peter
 * @version V1.0 创建时间：18/3/10
 *          Copyright 2018 by PreTang
 */
/*
*
Java语言系统自带有三个类加载器:
- Bootstrap ClassLoader 最顶层的加载类，主要加载核心类库，%JRE_HOME%\lib下的rt.jar、resources.jar、charsets.jar和class等。另外需要注意的是可以通过启动jvm时指定-Xbootclasspath和路径来改变Bootstrap ClassLoader的加载目录。比如java -Xbootclasspath/a:path被指定的文件追加到默认的bootstrap路径中。我们可以打开我的电脑，在上面的目录下查看，看看这些jar包是不是存在于这个目录。
- Extention ClassLoader 扩展的类加载器，加载目录%JRE_HOME%\lib\ext目录下的jar包和class文件。还可以加载-D java.ext.dirs选项指定的目录。
- Appclass Loader也称为SystemAppClass 加载当前应用的classpath的所有类。

我们看到了系统的3个类加载器，但我们可能不知道具体哪个先行呢？
我可以先告诉你答案
1. Bootstrap CLassloder
2. Extention ClassLoader
3. AppClassLoader
* */
public class ClassLoaderTest {


    public static void main(String[] args) {


//BootstrapClassLoader、ExtClassLoader、AppClassLoader实际是查阅相应的环境属性sun.boot.class.path、java.ext.dirs和java.class.path来加载资源文件的
        System.out.println(System.getProperty("sun.boot.class.path"));
        System.out.println(System.getProperty("java.ext.dirs"));
        System.out.println(System.getProperty("java.class.path"));

//
        ClassLoader cl = ClassLoaderTest.class.getClassLoader();
        System.out.println("ClassLoader is:"+cl.toString());
//        int.class是由Bootstrap ClassLoader加载的
//        cl = int.class.getClassLoader();
//        System.out.println("ClassLoader is:"+cl.toString());

//        每个类加载器都有一个父加载器
        System.out.println("ClassLoader\'s parent is:"+cl.getParent().toString());
        System.out.println("ClassLoader\'s grand father is:"+cl.getParent().getParent().toString());

//        自定义ClassLoader
//        不知道大家有没有发现，不管是Bootstrap ClassLoader还是ExtClassLoader等，这些类加载器都只是加载指定的目录下的jar包或者资源。如果在某种情况下，我们需要动态加载一些东西呢？比如从D盘某个文件夹加载一个class文件，或者从网络上下载class主内容然后再进行加载，这样可以吗？


    }

}
