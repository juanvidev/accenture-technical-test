package co.com.accenture.model.product;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class Product {
    private String id;
    private String name;
    private Integer stock;

    public Product() {
        this.id = UUID.randomUUID().toString();
    }

    public static class ProductBuilder {
        private String id = UUID.randomUUID().toString();
    }
}
