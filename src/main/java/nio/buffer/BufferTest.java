package nio.buffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;

/**
 * @author wangxueming
 * @create 2020-02-12 18:25
 * @description
 */
public class BufferTest {
    public static void main(String[] args) {
        testProperties();
        System.out.println("--------------------");
        testMark();
        System.out.println("--------------------");
        testPut();
        System.out.println("--------------------");
        testGet();
        System.out.println("--------------------");
        mixPutAndGet();
        System.out.println("--------------------");
        testCompact();
        System.out.println("--------------------");
        testDuplicate();
        System.out.println("--------------------");
        testSlice();
        System.out.println("--------------------");
        testPutAndGetElement();
        System.out.println("--------------------");
        testPutAndGetElement();
        testByteOrder();

    }

    private static void testByteOrder() {
        ByteBuffer buffer = ByteBuffer.allocate(12);
        // 直接存入一个int
        buffer.putInt(0x1234abcd);
        buffer.position(0);
        int big_endian = buffer.getInt();
        System.out.println(Integer.toHexString(big_endian));
        buffer.rewind();  //把position设为0，limit不变，一般在把数据重写入Buffer前调用
        int little_endian = buffer.order(ByteOrder.LITTLE_ENDIAN).getInt();
        System.out.println(Integer.toHexString(little_endian));
    }

    private static void testPutAndGetElement() {
        ByteBuffer buffer = ByteBuffer.allocate(12);
        // 直接存入一个int
        buffer.putInt(0x1234abcd);
        // 以byte分别取出
        buffer.position(0);
        byte b1 = buffer.get();
        byte b2 = buffer.get();
        byte b3 = buffer.get();
        byte b4 = buffer.get();
        System.out.println(Integer.toHexString(b1 & 0xff));
        System.out.println(Integer.toHexString(b2 & 0xff));
        System.out.println(Integer.toHexString(b3 & 0xff));
        System.out.println(Integer.toHexString(b4 & 0xff));
    }

    private static void testSlice() {
        CharBuffer buffer = CharBuffer.allocate(10);
        buffer.put("abcdefghij");
        buffer.position(5);
        CharBuffer slice = buffer.slice();
        showBuffer(buffer);
        showBuffer(slice);
    }

    private static void testDuplicate() {
        CharBuffer buffer = CharBuffer.allocate(10);
        CharBuffer buffer1 = buffer.duplicate();
        buffer.put("abcde");
        buffer1.put("alex");
        showBuffer(buffer);
        showBuffer(buffer1);
    }

    private static void testCompact() {
        CharBuffer buffer = CharBuffer.allocate(10);
        buffer.put("abcde");
        buffer.flip();
        // 先读取两个字符
        buffer.get();
        buffer.get();
        showBuffer(buffer);
        // 压缩
        buffer.compact();
        // 继续写入
        buffer.put("fghi");
        buffer.flip();
        showBuffer(buffer);
        // 从头读取后续的字符
        char[] chars = new char[buffer.remaining()];
        buffer.get(chars);
        System.out.println(chars);
    }
    private static void mixPutAndGet() {
        System.out.println("mix:");
        CharBuffer buffer = CharBuffer.allocate(10);
        buffer.put("abc");
        System.out.println(buffer.get());//position自动加1，为空
        buffer.put("def");
        showBuffer(buffer);
        // 读取此buffer的内容
        buffer.flip();
        char[] chars = new char[buffer.remaining()];
        buffer.get(chars);
        System.out.println(chars);
    }


    private static void testGet() {
        System.out.println("Get:");
        CharBuffer buffer = CharBuffer.allocate(10);
        buffer.put("abc");
        showBuffer(buffer);
        buffer.flip();
        // 第一种读取方法
        char c1 = buffer.get();
        char c2 = buffer.get();
        char c3 = buffer.get();
        //		char c4 = buffer.get();   //报错 java.nio.BufferUnderflowException
        showBuffer(buffer);
        buffer.clear();
        // 第二种读取方法
        buffer.put("abc");
        buffer.flip();
        char[] chars = new char[buffer.remaining()];
        buffer.get(chars);
        showBuffer(buffer);
        System.out.println(chars);
    }

    private static void testPut() {
        CharBuffer buffer = CharBuffer.allocate(10);
        //第一种put方法
        buffer.put('a').put('b').put('c');
        showBuffer(buffer);
        buffer.clear();
        //第二种put方法
        char[] chars = {'a', 'b', 'c'};
        buffer.put(chars);
        showBuffer(buffer);
        buffer.clear();
        //CharBuffer还可以使用String
        buffer.put("abc");
        showBuffer(buffer);
    }

    private static void testMark() {
        CharBuffer buffer = CharBuffer.allocate(10);
        showBuffer(buffer);
        // 设置mark位置为3
        buffer.position(3).mark().position(5);
        showBuffer(buffer);
        // reset后，position=mark
        buffer.reset();
        showBuffer(buffer);
    }

    /**
     * 测试Buffer的各种属性
     */
    private static void testProperties() {
        CharBuffer buffer = CharBuffer.allocate(10);
        // buffer的初始状态
        showBuffer(buffer);
        // 存入三个字符后的状态
        buffer.put("abc");
        showBuffer(buffer);
        // flip后的状态
        buffer.flip();
        //调用flip()之后，读/写指针position指到缓冲区头部，并且设置了最多只能读出之前写入的数据长度(而不是整个缓存的容量大小)
        showBuffer(buffer);
        // 读取两个字符后的状态
        char c1 = buffer.get();
        char c2 = buffer.get();
        System.out.println(c1);
        System.out.println(c2);
        showBuffer(buffer);
        // clear后的状态
        buffer.clear();//简单理解就是复位（Reset） 但不清除数据（position=0, limit=capacity）
        showBuffer(buffer);
    }
    /**
     * 显示buffer的position、limit、capacity和buffer中包含的字符，若字符为0，则替换为'.'
     *
     * @param buffer
     */
    private static void showBuffer(CharBuffer buffer) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buffer.limit(); i++) {
            char c = buffer.get(i);
            if (c == 0) {
                c = '.';
            }
            sb.append(c);
        }
        System.out.printf("position=%d, limit=%d, capacity=%d,content=%s\n"
                , buffer.position()
                , buffer.limit()
                , buffer.capacity()
                , sb.toString());
    }
}
