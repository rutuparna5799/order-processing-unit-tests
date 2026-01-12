package order_processing_engine;

import com.company.order_processing_engine.model.Customer;
import com.company.order_processing_engine.model.OrderItem;
import com.company.order_processing_engine.service.OrderServiceImpl;
import com.company.order_processing_engine.model.Order;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Order creation -Basic validation")
public class OrderServiceTest {

    private OrderServiceImpl orderService;

    @BeforeEach
    void setup() {
        orderService = new OrderServiceImpl();
    }


//    @Test
//    void test_createOrder_failTestCase(){
//        fail("Not implemented yet");
//    }


    @Test
    @DisplayName("Inactive customer should not be allowed to create order")
    void inactiveCustomerShouldFail() {

        // Arrange
        Customer customer = new Customer("C1", false, false);
        OrderItem item = new OrderItem("P1", 1, 1000);
        Order order = new Order("O1", customer, List.of(item), null, 0, 0, 0);

        // Act + Assert
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> orderService.createOrder(order),
                "Inactive customer must be blocked"
        );

        assertEquals("Customer is not active", ex.getMessage(),
                "Error message must explain the violation");
    }



}
