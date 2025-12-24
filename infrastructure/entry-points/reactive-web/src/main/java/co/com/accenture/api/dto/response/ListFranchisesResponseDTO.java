package co.com.accenture.api.dto.response;

import java.util.List;

public record ListFranchisesResponseDTO(
        String id,
        String name,
        List<ListSubsidiaryResponseDTO> subsidiaries
){

}
