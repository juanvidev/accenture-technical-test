package co.com.accenture.model.franchise;
import co.com.accenture.model.subsidary.Subsidiary;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Franchise {

    @Builder.Default
    private String id = UUID.randomUUID().toString();

    private String name;

    @Builder.Default
    private List<Subsidiary> subsidiaries = List.of();
}