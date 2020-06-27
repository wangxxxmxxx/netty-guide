package guide.protocol.http.xml.pojo;

public class OrderFactory {

    public static Order create(long orderID) {
        Order order = new Order();
        order.setOrderNumber(orderID);
        order.setTotal(9999.999f);
        Address address = new Address();
        address.setCity("上海市");
        address.setCountry("中国");
        address.setPostCode("050001");
        address.setState("上海市");
        address.setStreet1("浦东大道");
        order.setBillTo(address);
        Customer customer = new Customer();
        customer.setCustomerNumber(orderID);
        customer.setFirstName("W");
        customer.setLastName("XM");
        order.setCustomer(customer);
        order.setShipping(Shipping.INTERNATIONAL_MAIL);
        order.setShipTo(address);
        return order;
    }
}
