package lk.ijse.gdse.mini_project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class ItemDTO {

    private Integer id;
    private String name;
    private Double price;
    private Integer qty;

}
