package com.example.demo.Model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterDTO {

    String username;

    String jwtToken;

}