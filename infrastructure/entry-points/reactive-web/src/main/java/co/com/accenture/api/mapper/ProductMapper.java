package co.com.accenture.api.mapper;

import co.com.accenture.api.dto.request.CreateProductRequestDTO;
import co.com.accenture.model.product.Product;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    Product toDomain(CreateProductRequestDTO createProductRequestDTO);

    @AfterMapping
    default void setDefaultStock(@MappingTarget Product product) {
        if (product.getStock() == null) {
            product.setStock(0);
        }
    }

    default Product map(Product product) {

        String name = product.getName();
        Integer stock = product.getStock();

        return Product.builder()
                .name(name)
                .stock(stock)
                .build();
    }
}
