package co.com.accenture.model.subsidary;
import co.com.accenture.model.product.Product;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Subsidary {
    private Integer id;
    private String name;
    private List<Product> products;


}
