package guide.netty.codec.msgpack;

import org.msgpack.MessagePack;
import org.msgpack.template.Templates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangxueming
 * @create 2020-06-09 0:09
 * @description
 */
public class SimpleUse {
    public static void main(String[] args) throws IOException {
        //序列化
        List<String> src = new ArrayList<String>();
        src.add("aaa");
        src.add("bbb");
        src.add("ccc");
        MessagePack msgpack = new MessagePack();
        byte[] raw = msgpack.write(src);

        //反序列化
        List<String> src2 = (List<String>) msgpack.read(raw, Templates.tList(Templates.TString));
        System.out.println(src2.get(2));
    }
}
