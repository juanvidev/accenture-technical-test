package co.com.accenture.model.product;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Product {

    @Builder.Default
    private String id = UUID.randomUUID().toString();

    private String name;

    @Builder.Default
    private Integer stock = 0;
}
