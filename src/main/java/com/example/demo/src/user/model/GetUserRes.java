package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class GetUserRes {
    private int userIdx;
    private String name;
    private String nickName;
    private String email;
}
