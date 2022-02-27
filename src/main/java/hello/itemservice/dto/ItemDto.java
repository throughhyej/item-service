package hello.itemservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemDto {

    private String itemName;
    private Integer price;
    private Integer quantity;

    public ItemDto () {}
    public ItemDto (String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

}
