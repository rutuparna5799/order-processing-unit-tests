package order_processing_engine;

import com.company.order_processing_engine.exception.OrderAmountLimitExceededException;
import com.company.order_processing_engine.model.Customer;
import com.company.order_processing_engine.model.Order;
import com.company.order_processing_engine.model.OrderItem;
import com.company.order_processing_engine.model.OrderStatus;
import com.company.order_processing_engine.service.ComplianceServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@DisplayName("Compliance Validation")
public class ComplianceServiceTest {
    @InjectMocks
    private ComplianceServiceImpl complianceService;

    @Test
    @DisplayName("Compliance – Order exceeding ₹5L must fail")
    void testValidate_shouldFail_whenOrderTotalExceedsComplianceLimit() {
        OrderItem item = new OrderItem("P1", 10, 60000); // 6L
        Order order = new Order("O1",
                new Customer("C1", true, false),
                List.of(item),
                OrderStatus.CREATED,
                0,0,0);

        OrderAmountLimitExceededException ex =
                assertThrows(OrderAmountLimitExceededException.class,
                        () -> complianceService.validate(order));

        assertEquals("Total amount must not exceed 500000", ex.getMessage());
    }

}
