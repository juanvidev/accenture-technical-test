package co.com.accenture.api.dto.response;

import java.util.List;

public record ListSubsidiaryResponseDTO(
        String id,
        String name,
        List<CreateProductResponseDTO> products
){

}
