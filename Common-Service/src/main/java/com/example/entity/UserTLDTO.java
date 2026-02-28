package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserTLDTO {
    private String id;
    private String username;
    private String role;
}
