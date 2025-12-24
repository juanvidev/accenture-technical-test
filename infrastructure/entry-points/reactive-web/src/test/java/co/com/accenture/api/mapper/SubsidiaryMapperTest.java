package co.com.accenture.api.mapper;

import co.com.accenture.api.dto.request.CreateSubsidiaryRequestDTO;
import co.com.accenture.api.dto.response.CreateSubsidiaryResponseDTO;
import co.com.accenture.model.subsidary.Subsidiary;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubsidiaryMapperTest {

    private final SubsidiaryMapper mapper = Mappers.getMapper(SubsidiaryMapper.class);

    @Test
    @DisplayName("Should map CreateSubsidiaryRequestDTO to Subsidiary entity correctly")
    void testToDomain() {
        CreateSubsidiaryRequestDTO dto = new CreateSubsidiaryRequestDTO(
                "Cali",
                new ArrayList<>(List.of())
        );

        Subsidiary subsidiary = mapper.toDomain(dto);

        assertNotNull(subsidiary);
        assertEquals(dto.name(), subsidiary.getName());
    }

    @Test
    @DisplayName("Should map Subsidiary entity to CreateSubsidiaryRequestDTO correctly")
    void testToResponse() {
        Subsidiary subsidiary = Subsidiary.builder()
                .name("Subway")
                .build();

        CreateSubsidiaryResponseDTO response = mapper.toResponse(subsidiary);

        assertNotNull(response);
        assertEquals(subsidiary.getName(), response.name());
    }


    @Test
    @DisplayName("Should handle null values in mapping methods")
    void testNullMapping() {
        assertNull(mapper.toDomain(null));
        assertNull(mapper.toResponse(null));
    }

}