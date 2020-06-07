package nio.channel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author wangxueming
 * @create 2020-02-13 0:35
 * @description
 */
public class FileLockExample {
    final static String filepath = FileChannelExample.class.getResource("/").getPath() + "nio/FileLockExample.txt";
    private static Random rand = new Random();
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: [-r | -w]");
            System.exit(1);
        }
        boolean isWriter = args[0].equals("-w");
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(filepath, (isWriter) ? "rw" : "r");
            FileChannel channel = randomAccessFile.getChannel();
            if (isWriter) {
                lockAndWrite(channel);
            } else {
                lockAndRead(channel);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void lockAndWrite(FileChannel channel) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(4);
            int i = 0;
            while (true) {
                System.out.println("Writer try to lock file...");
                FileLock lock = channel.lock(0, 4, false);
                buffer.putInt(0, i);
                buffer.position(0).limit(4);
                System.out.println("buffer is :" + buffer);
                channel.write(buffer, 0);
                channel.force(true);//将通道里尚未写入磁盘的数据强制写到磁盘上。出于性能方面的考虑，操作系统会将数据缓存在内存中，
                //所以无法保证写入到FileChannel里的数据一定会即时写到磁盘上。要保证这一点，需要调用force()方法
                //force()方法有一个boolean类型的参数，指明是否同时将文件元数据（权限信息等）写到磁盘上
                buffer.clear();
                System.out.println("Writer write :" + i++);
                lock.release();
                System.out.println("Sleeping...");
                TimeUnit.SECONDS.sleep(rand.nextInt(3));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void lockAndRead(FileChannel channel) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(4);
            while (true) {
                System.out.println("Reader try to lock file...");
                FileLock lock = channel.lock(0, 4, true);
                buffer.clear();
                channel.read(buffer, 0);
                buffer.flip();
                System.out.println("buffer is:" + buffer);
                int i = buffer.getInt(0);
                System.out.println("Reader read :" + i);
                lock.release();
                System.out.println("Sleeping...");
                TimeUnit.SECONDS.sleep(rand.nextInt(3));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
