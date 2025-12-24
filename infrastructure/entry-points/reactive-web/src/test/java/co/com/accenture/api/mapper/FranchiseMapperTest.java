package co.com.accenture.api.mapper;

import co.com.accenture.api.dto.request.CreateFranchiseRequestDTO;
import co.com.accenture.api.dto.response.CreateFranchiseResponseDTO;
import co.com.accenture.model.franchise.Franchise;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FranchiseMapperTest {

    private final FranchiseMapper mapper = Mappers.getMapper(FranchiseMapper.class);

    @Test
    @DisplayName("Should map CreateFranchiseRequestDTO to Franchise entity correctly")
    void testToDomain() {
        CreateFranchiseRequestDTO dto = new CreateFranchiseRequestDTO(
                "Subway",
                new ArrayList<>(List.of())
        );

        Franchise franchise = mapper.toDomain(dto);

        assertNotNull(franchise);
        assertEquals(dto.name(), franchise.getName());
    }

    @Test
    @DisplayName("Should map Franchise entity to CreateFranchiseRequestDTO correctly")
    void testToResponse() {
        Franchise franchise = Franchise.builder()
                .name("Subway")
                .build();

        CreateFranchiseResponseDTO response = mapper.toResponse(franchise);

        assertNotNull(response);
        assertEquals(franchise.getName(), response.name());
    }


    @Test
    @DisplayName("Should handle null values in mapping methods")
    void testNullMapping() {
        assertNull(mapper.toDomain(null));
        assertNull(mapper.toResponse(null));
    }

}