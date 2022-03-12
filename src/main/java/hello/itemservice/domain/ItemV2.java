package hello.itemservice.domain;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemV2 extends Item {

    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;

    public ItemV2 () {}
    public ItemV2 (String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
