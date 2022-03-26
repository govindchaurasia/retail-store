package com.retail.store.service;

import com.retail.store.dto.InvoiceRequest;
import com.retail.store.dto.ProductDTO;
import com.retail.store.entity.Invoice;
import com.retail.store.entity.Product;
import com.retail.store.entity.UserMaster;
import com.retail.store.enums.ProductCategory;
import com.retail.store.repository.InvoiceRepository;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InvoiceServiceTest {
    private InvoiceService invoiceService;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Before
    public void setUp() {
        invoiceService = new InvoiceService(userService, productService, invoiceRepository);
    }

    @Test
    public void shouldGenerateInvoiceWhenUserIsNew() {
        long userId = 1L;
        Invoice expectedInvoice = new Invoice();
        expectedInvoice.setId(101L);
        UserMaster expectedUserMaster = new UserMaster();
        expectedUserMaster.setId(userId);

        InvoiceRequest invoiceRequest = new InvoiceRequest();
        invoiceRequest.setUserId(userId);
        invoiceRequest.setProductDto(asList(createProductDTO(1, 10), createProductDTO(2, 8)
                , createProductDTO(3, 5), createProductDTO(4, 15)));
        Map<Long, Product> productMap = createProductMap();

        when(userService.findUserById(1L)).thenReturn(expectedUserMaster);
        when(productService.findByIds(Mockito.anyList())).thenReturn(productMap);
        when(invoiceRepository.save(Mockito.any(Invoice.class))).thenReturn(expectedInvoice);

        Invoice invoice = invoiceService.generateInvoice(invoiceRequest);

        assertThat(invoice.getId(), Is.is(101L));
    }

    private Map<Long, Product> createProductMap() {
        Map<Long, Product> productMap = new HashMap<>();
        productMap.put(1L, new Product("OIL", ProductCategory.GROCERY, "PACKET", 100));
        productMap.put(2L, new Product("Protein Shake", ProductCategory.HEALTH, "KG", 500));
        productMap.put(3L, new Product("Shaving Cream", ProductCategory.BEAUTY, "PACKET", 120));
        productMap.put(4L, new Product("Shaving Lotion", ProductCategory.BEAUTY, "PACKET", 110));
        return productMap;
    }

    private ProductDTO createProductDTO(long productId, int qty) {
        return new ProductDTO(productId, qty);
    }
}