package order_processing_engine;

import com.company.order_processing_engine.exception.*;
import com.company.order_processing_engine.model.Customer;
import com.company.order_processing_engine.model.OrderItem;
import com.company.order_processing_engine.model.OrderStatus;
import com.company.order_processing_engine.repository.OrderRepository;
import com.company.order_processing_engine.service.ComplianceService;
import com.company.order_processing_engine.service.OrderServiceImpl;
import com.company.order_processing_engine.model.Order;
import com.company.order_processing_engine.service.PricingService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Order creation -Basic validation")
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ComplianceService complianceService;

    @Mock
    private PricingService pricingService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;


    @BeforeEach
    void init() {

    }


//    @Test
//    void test_createOrder_failTestCase(){
//        fail("Not implemented yet");
//    }


    @Test
    @DisplayName("Inactive customer should not be allowed to create order")
    void testCreateOrder_inactiveCustomer_ShouldFail() {

        // Arrange
        Customer customer = new Customer("C1", false, false);
        OrderItem item = new OrderItem("P1", 1, 1000);
        Order order = new Order("O1", customer, List.of(item), null, 0, 0, 0);

        // Act + Assert
        CustomerInactiveException ex = assertThrows(
                CustomerInactiveException.class,
                () -> orderService.createOrder(order)
        );

        assertEquals("Customer is not active", ex.getMessage(),
                "Error message must explain the violation");
    }

    @Test
    @DisplayName("Order has at least one item")
    @org.junit.jupiter.api.Order(2)
    void testCreateOrder_noItems_shouldFail(){

        //Arrange
        Customer customer= new Customer("C1", true, false);
        Order order= new Order("O1", customer, List.of(), null, 0, 0, 0);

        String expectedResult="Order must contain at least one item";

        //act
        EmptyOrderItemsException actualResult = assertThrows(
                EmptyOrderItemsException.class,
                () -> orderService.createOrder(order)
        );
        // Assert the message
        assertEquals(
                expectedResult,
                actualResult.getMessage(),
                "Proper validation message must be returned"
        );

    }

    @Test
    @DisplayName("Item Price must be > 0")
    @org.junit.jupiter.api.Order(3)
    void testCreateOrder_PriceZero_shouldFail(){
        //Arrange
        Customer customer = new Customer("C1", true, false);
        OrderItem item = new OrderItem("P1", 1, 0);

        order = new Order(
                "ORD-1",
                customer,
                List.of(item),
                OrderStatus.CREATED,
                0,
                0,
                0
        );
        String expectedResult="Item Price must be > 0";
        //Act
        InvalidItemPriceException actualResult=assertThrows(
                InvalidItemPriceException.class,
                () -> orderService.createOrder(order)
        );

        //Assert
        assertEquals(expectedResult,actualResult.getMessage());


    }

    @Test
    @DisplayName("ItemPrice must be > 0")
    @org.junit.jupiter.api.Order(4)
    void testCreateOrder_CouldCreateOrder_WhenItemPriceIsGreaterThanZero()
    {
        //Arrange
        Customer customer= new Customer("c1",true, false);
        OrderItem orderItem=new OrderItem("p1",10,100);
        Order order= new Order("O1",customer,List.of(orderItem),OrderStatus.CREATED,10000,0,0);
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        //Act
        Order created= orderService.createOrder(order);

        //assert
       assertNotNull(created,"Order must not be null");
       assertEquals(OrderStatus.CREATED,created.getStatus(),"Order status must be created");
       assertEquals(1000,created.getTotalAmount(),"Total must be price * quantity");

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("Order Quantity>0")
    @org.junit.jupiter.api.Order(5)
    void testCreateOrde_orderQuantity_mustNotZero() {
        //Arrange
        Customer customer= new Customer("C1",true, false);
        OrderItem orderItem =new OrderItem("p1",0,70);
        Order order=new Order("O1",customer,List.of(orderItem),OrderStatus.CREATED,10000,0,0);

        //Act
        InvalidQuantityException actualResult= assertThrows(InvalidQuantityException.class,
                ()-> orderService.createOrder(order));

        //Assert
        assertEquals("Order Quantity mut be > 0", actualResult.getMessage());

    }




    @Test
    @org.junit.jupiter.api.Order(6)
    @DisplayName("Order exceeding ₹5L must fail compliance")
    void testCreateOrder_totalAmount_mustNotExceedLimit() {

        // Arrange
        Customer customer = new Customer("C1", true, false);

        // 10 × 60000 = 600000 (> 500000)
        OrderItem item = new OrderItem("P1", 10, 60000);

        Order order = new Order(
                "O1",
                customer,
                List.of(item),
                OrderStatus.CREATED,
                0,
                0,
                0
        );


        // Act
        doThrow(new OrderAmountLimitExceededException())
                .when(complianceService).validate(any(Order.class));

        OrderAmountLimitExceededException exception = assertThrows(OrderAmountLimitExceededException.class,
                () -> orderService.createOrder(order));

        // Assert
        assertEquals("Total amount must not exceed 500000", exception.getMessage());
    }



    @Test
    @DisplayName("Cancel Order – Order must exist")
    @org.junit.jupiter.api.Order(7)
    void cancelOrder_shouldFail_whenOrderDoesNotExist() {

        // Arrange
        when(orderRepository.findById("O1")).thenReturn(Optional.empty());

        // Act
        OrderNotFoundException ex = assertThrows(OrderNotFoundException.class,
                () -> orderService.cancelOrder("O1", "Customer requested"));

        // Assert
        assertEquals("Order not found", ex.getMessage());
    }


    @Test
    @DisplayName("Cancel Order – Must be in CREATED state")
    @org.junit.jupiter.api.Order(8)
    void cancelOrder_shouldFail_whenOrderNotInCreatedState() {

        // Arrange
        Customer customer = new Customer("C1", true, false);
        Order order = new Order("O1", customer, List.of(), OrderStatus.CANCELLED, 1000, 0, 0);

        when(orderRepository.findById("O1")).thenReturn(Optional.of(order));

        // Act
        InvalidOrderStateException ex = assertThrows(InvalidOrderStateException.class,
                () -> orderService.cancelOrder("O1", "Customer requested"));

        // Assert
        assertEquals("Only CREATED orders can be cancelled", ex.getMessage());
    }


//    @Test
//    @DisplayName("Cancel Order – Cannot cancel already cancelled order")
//    @org.junit.jupiter.api.Order(9)
//    void cancelOrder_shouldFail_whenOrderAlreadyCancelled() {
//
//        // Arrange
//        Customer customer = new Customer("C1", true, false);
//        Order order = new Order("O1", customer, List.of(), OrderStatus.CANCELLED, 1000, 0, 0);
//
//        when(orderRepository.findById("O1")).thenReturn(Optional.of(order));
//
//        // Act
//        RuntimeException ex = assertThrows(RuntimeException.class,
//                () -> orderService.cancelOrder("O1", "Mistake"));
//
//        // Assert
//        assertEquals("Order already cancelled", ex.getMessage());
//    }


    @Test
    @DisplayName("Cancel Order – Cancellation reason is mandatory")
    @org.junit.jupiter.api.Order(10)
    void cancelOrder_shouldFail_whenReasonIsMissing() {

        // Arrange
        Customer customer = new Customer("C1", true, false);
        Order order = new Order("O1", customer, List.of(), OrderStatus.CREATED, 1000, 0, 0);

        when(orderRepository.findById("O1")).thenReturn(Optional.of(order));

        // Act
        MissingCancellationReasonException ex = assertThrows(MissingCancellationReasonException.class,
                () -> orderService.cancelOrder("O1", null));

        // Assert
        assertEquals("Cancellation reason is required", ex.getMessage());
    }


    @Test
    @DisplayName("Cancel Order – Successful cancellation")
    @org.junit.jupiter.api.Order(11)
    void cancelOrder_shouldCancelSuccessfully() {

        // Arrange
        Customer customer = new Customer("C1", true, false);
        Order order = new Order("O1", customer, List.of(), OrderStatus.CREATED, 1000, 0, 0);

        when(orderRepository.findById("O1")).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Order cancelledOrder = orderService.cancelOrder("O1", "Customer requested");

        // Assert
        assertEquals(OrderStatus.CANCELLED, cancelledOrder.getStatus());
    }

    @Test
    @DisplayName("Get Order – Order must exist")
    void getOrderById_shouldFail_whenOrderNotFound() {
        //Arrange
        when(orderRepository.findById("O1")).thenReturn(Optional.empty());

        //Act
        OrderNotFoundException ex = assertThrows(OrderNotFoundException.class,
                () -> orderService.getOrderById("O1"));


        //Assert
        assertEquals("Order not found", ex.getMessage());
    }

    @Test
    @DisplayName("Get Order – Should return order when exists")
    void getOrderById_shouldReturnOrder() {
        Customer customer = new Customer("C1", true, false);
        Order order = new Order("O1", customer, List.of(), OrderStatus.CREATED, 1000, 0, 0);

        when(orderRepository.findById("O1")).thenReturn(Optional.of(order));

        Order result = orderService.getOrderById("O1");

        assertEquals("O1", result.getOrderId());
    }

    @Test
    @DisplayName("Get Orders By Customer – Customer ID is null")
    void getOrdersByCustomer_shouldFail_whenCustomerIdIsNull() {
        //Act
        CustomerIdRequiredException ex = assertThrows(CustomerIdRequiredException.class,
                () -> orderService.getOrdersByCustomer(null));

        //Assert
        assertEquals("Customer ID is required", ex.getMessage());
    }


    @Test
    @DisplayName("Get Orders By Customer – No orders found")
    void getOrdersByCustomer_shouldReturnEmptyList_whenNoOrders() {
        when(orderRepository.findByCustomerId("C1")).thenReturn(List.of());

        List<Order> orders = orderService.getOrdersByCustomer("C1");

        assertNotNull(orders);
        assertEquals(0, orders.size());
    }


    @Test
    @DisplayName("Get Orders By Customer – Should return orders")
    void getOrdersByCustomer_shouldReturnOrders() {
        Customer customer = new Customer("C1", true, false);

        List<Order> mockOrders = List.of(
                new Order("O1", customer, List.of(), OrderStatus.CREATED, 1000, 0, 0),
                new Order("O2", customer, List.of(), OrderStatus.CANCELLED, 500, 0, 0)
        );

        when(orderRepository.findByCustomerId("C1")).thenReturn(mockOrders);

        List<Order> orders = orderService.getOrdersByCustomer("C1");

        assertEquals(2, orders.size());
    }












}
