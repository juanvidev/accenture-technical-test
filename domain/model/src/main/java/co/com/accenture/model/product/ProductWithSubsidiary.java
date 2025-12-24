package co.com.accenture.model.product;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductWithSubsidiary {
    private String productId;
    private String productName;
    private Integer stock;
    private String subsidiaryName;
}
