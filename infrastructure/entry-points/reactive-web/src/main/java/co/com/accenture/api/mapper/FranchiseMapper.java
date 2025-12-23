package co.com.accenture.api.mapper;

import co.com.accenture.api.dto.request.CreateFranchiseRequestDTO;
import co.com.accenture.api.dto.response.CreateFranchiseResponseDTO;
import co.com.accenture.model.franchise.Franchise;
import co.com.accenture.model.subsidary.Subsidiary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface FranchiseMapper {

    CreateFranchiseResponseDTO toResponse(Franchise franchise);

    @Mapping(target = "id", ignore = true)
    Franchise toDomain(CreateFranchiseRequestDTO createFranchiseRequestDTO);

    default List<Subsidiary> map(List<String> subsidiaries) {
        if (subsidiaries == null) {
            return List.of();
        }

        return subsidiaries.stream()
                .map(name -> Subsidiary.builder()
                        .name(name)
                        .build())
                .toList();
    }
}
