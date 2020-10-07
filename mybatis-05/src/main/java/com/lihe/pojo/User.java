package com.lihe.pojo;

import lombok.*;
import org.apache.ibatis.type.Alias;

// 实体类

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Alias("user")
public class User {
    private int id;
    private String name;
    private String password;
}
