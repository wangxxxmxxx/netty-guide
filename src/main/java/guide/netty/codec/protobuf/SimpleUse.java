package guide.netty.codec.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangxueming
 * @create 2020-06-10 01:53
 * @description
 */
public class SimpleUse {
    private static byte[] encode(SubscribeReqProto.SubscribeReq req) {
        return req.toByteArray();
    }

    private static SubscribeReqProto.SubscribeReq decode(byte[] body)
            throws InvalidProtocolBufferException {
        return SubscribeReqProto.SubscribeReq.parseFrom(body);
    }

    private static SubscribeReqProto.SubscribeReq createSubscribeReq() {
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();

        builder.setSubReqID(1);
        builder.setUserName("...");
        builder.setProductName("product");

        List<String> address = new ArrayList<String>();
        address.add("shanghai");
        address.add("suzhou");
        address.add("wuxi");
        builder.addAllAddress(address);

        return builder.build();
    }

    public static void main(String[] args) throws InvalidProtocolBufferException {
        SubscribeReqProto.SubscribeReq req = createSubscribeReq();
        System.out.println("Before encode : " + req.toString());
        SubscribeReqProto.SubscribeReq req2 = decode(encode(req));
        System.out.println("After encode : " + req2.toString());
        System.out.println("Assert address equal : --> " + (req2 == req));
        System.out.println("Assert equal : --> " + req2.equals(req));
    }
}
