package order_processing_engine;

import com.company.order_processing_engine.service.PricingService;
import com.company.order_processing_engine.service.PricingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class PricingServiceTest {
    private PricingService pricingService;

    @BeforeEach
    void setUp(){
        pricingService=new PricingServiceImpl();
    }

    @ParameterizedTest(name = "Total={0}, Premium={1}, Festival={2} → {3}%")
    @DisplayName("Discount Rules from CSV")
    @CsvFileSource(resources = "/discount-rules.csv", numLinesToSkip = 1)
    void testCalculateDiscount_DiscountRules_fromCSV(double baseTotal,
                                                     boolean isPremium,
                                                     boolean festival,
                                                     double expectedDiscountPercent) {

        // Act
        double discount = pricingService.calculateDiscount(baseTotal, isPremium, festival);

        // Assert
        double expectedDiscount = baseTotal * (expectedDiscountPercent / 100.0);

        assertEquals(expectedDiscount,
                discount,
                0.01,
                "Discount must match CSV business rule");
    }


    @ParameterizedTest(name = "Base={0}, Discount={1} → Expected GST={2}")
    @DisplayName("GST = 18% applied after discount")
    @org.junit.jupiter.params.provider.MethodSource("taxTestCases")
    void testCalculateTax(double baseAmount,
                          double discount,
                          double expectedTax) {

        double tax = pricingService.calculateTax(baseAmount, discount);

        assertEquals(expectedTax, tax, 0.01);
    }

    //  DATA PROVIDER
    static Stream<Arguments> taxTestCases() {
        return Stream.of(
                Arguments.of(1000.0, 100.0, 162.0),
                Arguments.of(500.0, 0.0, 90.0),
                Arguments.of(2000.0, 200.0, 324.0),
                Arguments.of(999.99, 0.0, 180.0),
                Arguments.of(100.0, 33.33, 12.0)
        );
    }

}
