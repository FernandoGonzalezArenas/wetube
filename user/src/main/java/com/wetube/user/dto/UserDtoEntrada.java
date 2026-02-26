package com.wetube.user.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDtoEntrada {

    @Size(max = 1000)
private String bio;
private String profilePictureUrl;

}
