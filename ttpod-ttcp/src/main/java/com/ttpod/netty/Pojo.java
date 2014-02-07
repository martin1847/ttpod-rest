package com.ttpod.netty;


/**
 * date: 14-2-6 下午7:46
 *
 * @author: yangyang.cong@ttpod.com
 */
public class Pojo {


    String name;

    int age;
    public Pojo(){}
    public Pojo(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Pojo{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
