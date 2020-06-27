package guide.protocol.http.xml.xmlcompile;

import org.jibx.binding.Compile;
import org.jibx.binding.generator.BindGen;
import org.jibx.runtime.JiBXException;

import java.io.IOException;

public class GenBindFileTool {

    /**
     * 注意：jibx_1_2_x在jdk1.8的环境下会出错，jdk1.8建议下载jibx_1_3_x
     */
    public static void main(String[] args) throws JiBXException, IOException {

        genBindFiles();
        compile();
    }

    private static void compile() {
        String[] args = new String[2];

        // 打印生成过程的详细信息。可选
        args[0] = "-v";

        // 指定 binding 和 schema 文件的路径。必须
        args[1] = "./src/main/resources/guide/protocol/http/xml/pojo/binding.xml";

        Compile.main(args);
    }

    private static void genBindFiles() throws JiBXException, IOException {
        String[] args = new String[9];

        // 指定pojo源码路径（指定父包也是可以的）。必须
        args[0] = "-s";
        args[1] = "./src/main/java/guide/protocol/http/xml/pojo";

        // 自定义生成的binding文件名，默认文件名binding.xml。可选
        args[2] = "-b";
        args[3] = "binding.xml";

        // 打印生成过程的一些信息。可选
        args[4] = "-v";

        // 如果目录已经存在，就删除目录。可选
        args[5] = "-w";

        // 指定输出路径。默认路径 .（当前目录,即根目录）。可选
        args[6] = "-t";
        args[7] = "./src/main/resources/guide/protocol/http/xml/pojo";


        // 告诉 BindGen 使用下面的类作为 root 生成 binding 和 schema。必须
//        args[8] = "guide.protocol.http.xml.pojo.Customer guide.protocol.http.xml.pojo.Address guide.protocol.http.xml.pojo.Order guide.protocol.http.xml.pojo.Shipping";
        args[8] = "guide.protocol.http.xml.pojo.Order";

        BindGen.main(args);
    }
}
