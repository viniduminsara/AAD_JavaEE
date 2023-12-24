package lk.ijse.gdse.mini_project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class CustomerDTO {

    private Integer id;
    private String name;
    private String city;
    private String email;

}
