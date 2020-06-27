package guide.protocol.http.xml;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import guide.protocol.http.xml.pojo.Order;
import guide.protocol.http.xml.pojo.OrderFactory;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;

public class TestOrder {
    private IBindingFactory factory = null;
    private StringWriter writer = null;
    private StringReader reader = null;
    private final static String CHARSET_NAME = "UTF-8";

    private String encode2Xml(Order order) throws JiBXException, IOException {
        //构造IbindingFactory
        factory = BindingDirectory.getFactory(Order.class);

        //构造Marshalling上下文
        writer = new StringWriter();
        IMarshallingContext mctx = factory.createMarshallingContext();
        mctx.setIndent(2);
        mctx.marshalDocument(order, CHARSET_NAME, null, writer);
        String xmlStr = writer.toString();
        writer.close();

        //打印
        System.out.println(xmlStr);
        return xmlStr;
    }

    private Order decode2Order(String xmlBody) throws JiBXException {
        reader = new StringReader(xmlBody);

        //上下文
        IUnmarshallingContext uctx = factory.createUnmarshallingContext();
        Order order = (Order) uctx.unmarshalDocument(reader);
        return order;
    }

    public static void main(String[] args) throws JiBXException, IOException {
        TestOrder test = new TestOrder();
        Order order = OrderFactory.create(0);
        //编码
        String body = test.encode2Xml(order);
        //解码
        Order order2 = test.decode2Order(body);
        System.out.println(order2);
    }
}
