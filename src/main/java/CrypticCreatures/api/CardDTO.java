package CrypticCreatures.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CardDTO {
    @JsonProperty("Id")
    private String id;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Damage")
    private double damage;
}
