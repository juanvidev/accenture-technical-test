package co.com.accenture.api.mapper;

import co.com.accenture.api.dto.request.CreateSubsidiaryRequestDTO;
import co.com.accenture.model.product.Product;
import co.com.accenture.model.subsidary.Subsidiary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface SubsidiaryMapper {

//    CreateSubsidiaryResponseDTO toResponse(Subsidiary subsidiary);

    @Mapping(target = "id", ignore = true)
    Subsidiary toDomain(CreateSubsidiaryRequestDTO createSubsidiaryRequestDTO);

    default List<Product> map(List<String> products) {
        if (products == null) {
            return List.of();
        }

        return products.stream()
                .map(name -> Product.builder()
                        .name(name)
                        .build())
                .toList();
    }
}
