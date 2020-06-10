package guide.netty.codec.msgpack.usage;

import org.msgpack.annotation.Message;


/**
 * @author wangxueming
 * @create 2020-06-09 22:44
 * @description
 */
@Message //msgpack必须加注解
public class UserInfo {
    private String name;
    private int age;

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
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
