package co.com.accenture.model.subsidary;
import co.com.accenture.model.product.Product;
import lombok.*;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class Subsidiary {
    private String id;
    private String name;
    @Builder.Default
    private List<Product> products = List.of();

    public Subsidiary() {
        this.id = UUID.randomUUID().toString();
    }

    public static class SubsidiaryBuilder {
        private String id = UUID.randomUUID().toString();
    }

}
