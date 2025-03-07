package CrypticCreatures.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Trade {
    @JsonProperty("Id")
    private String tid;
    @JsonProperty("CardToTrade")
    private String offeredCardId;
    private int uid;
    @JsonProperty("Type")
    private String requiredType;
    @JsonProperty("MinimumDamage")
    private int minDamage;


    public Trade(String tradeId, String offeredCardId, String requiredType, int minDamage) {
        this.tid = tradeId;
        this.offeredCardId = offeredCardId;
        this.requiredType = requiredType;
        this.minDamage = minDamage;
    }
    public Trade(String tradeId, int uid, String offeredCardId, String requiredType, int minDamage) {
        this.tid = tradeId;
        this.uid = uid;
        this.offeredCardId = offeredCardId;
        this.requiredType = requiredType;
        this.minDamage = minDamage;
    }
}
