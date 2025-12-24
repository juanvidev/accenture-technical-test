package co.com.accenture.api.mapper;

import co.com.accenture.api.dto.request.CreateProductRequestDTO;
import co.com.accenture.api.dto.response.CreateProductResponseDTO;
import co.com.accenture.model.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    private final ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

    @Test
    @DisplayName("Should map CreateProductRequestDTO to Product entity correctly")
    void testToDomain() {
        CreateProductRequestDTO dto = new CreateProductRequestDTO(
                "Cali",
                5
        );

        Product product = mapper.toDomain(dto);

        assertNotNull(product);
        assertEquals(dto.name(), product.getName());
    }

    @Test
    @DisplayName("Should map Product entity to CreateProductRequestDTO correctly")
    void testToResponse() {
        Product product = Product.builder()
                .name("Subway")
                .build();

        CreateProductResponseDTO response = mapper.toResponse(product);

        assertNotNull(response);
        assertEquals(product.getName(), response.name());
    }


    @Test
    @DisplayName("Should handle null values in mapping methods")
    void testNullMapping() {
        assertNull(mapper.toDomain(null));
        assertNull(mapper.toResponse(null));
    }

}