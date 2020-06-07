package nio.channel;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author wangxueming
 * @create 2020-02-13 0:17
 * @description
 */
public class FileChannelExample {
    private static String fileStr = FileChannelExample.class.getResource("/").getPath() + "nio/FileChannelExample.txt";
    public static void main(String[] args) throws IOException {
        testChannelCreate();
        System.out.println("--------------");
        testFilePosition();
        System.out.println("--------------");
        testFileHole();
        System.out.println("--------------");
        testFileCopy();

    }

    private static void testFileCopy() throws IOException {
        RandomAccessFile source = new RandomAccessFile(fileStr, "r");
        RandomAccessFile dest = new RandomAccessFile(fileStr + "1", "rw");
        FileChannel srcChannel = source.getChannel();
        FileChannel destChannel = dest.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(8);
        while (srcChannel.read(buffer) != -1) {
            buffer.flip();
            while (buffer.hasRemaining()) {
                destChannel.write(buffer);
            }
            buffer.clear();
        }
        srcChannel.close();
        destChannel.close();
    }

    private static void testFileHole() {
        final String filepath = fileStr;
        try {
            //create a file with 26 char a~z
            FileOutputStream fos = new FileOutputStream(filepath);
            StringBuilder sb = new StringBuilder();
            for (char c = 'a'; c <= 'z'; c++) {
                sb.append(c);
            }
            fos.write(sb.toString().getBytes());
            fos.flush();
            fos.close();
            //creat FileChannel
            RandomAccessFile file = new RandomAccessFile(filepath, "rw");
            System.out.println("file length is:"+file.length());
            FileChannel channel = file.getChannel();
            //wirte a byte at position 100
            channel.position(100);
            ByteBuffer bf = ByteBuffer.allocate(1);
            int index = channel.read(bf);
            System.out.println("index:" + index + "   read byte:" + bf.get(0));
            System.out.println("file position in RandomAccessFile is :" + file.getFilePointer());
            System.out.println("file length is:"+file.length());
            channel.write((ByteBuffer) ByteBuffer.allocate(1).put((byte) 0).flip());
            System.out.println("file position in RandomAccessFile is :" + file.getFilePointer());
            System.out.println("file length is:"+file.length());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void testFilePosition() {
        final String filepath = fileStr;
        try {
            //create a file with 26 char a~z
            FileOutputStream fos = new FileOutputStream(filepath);
            StringBuilder sb = new StringBuilder();
            for (char c = 'a'; c <= 'z'; c++) {
                sb.append(c);
            }
            fos.write(sb.toString().getBytes());
            fos.flush();
            fos.close();
            //creat FileChannel
            RandomAccessFile file = new RandomAccessFile(filepath, "rw");
            FileChannel channel = file.getChannel();
            System.out.println("file position in FileChannel is :" + channel.position());
            file.seek(5);
            System.out.println("file position in FileChannel is :" + channel.position());
            channel.position(10);
            System.out.println("file position in RandomAccessFile is :" + file.getFilePointer());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void testChannelCreate() throws IOException {
        final String filepath = fileStr;
        RandomAccessFile randomAccessFile = new RandomAccessFile(filepath, "rw");
        FileChannel readAndWriteChannel = randomAccessFile.getChannel();
        FileInputStream fis = new FileInputStream(filepath);
        FileChannel readChannel = fis.getChannel();
        FileOutputStream fos = new FileOutputStream(filepath);
        FileChannel writeChannel = fos.getChannel();
        readAndWriteChannel.close();
        readChannel.close();
        writeChannel.close();
    }

}
