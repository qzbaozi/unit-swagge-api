package com.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sfh
 * @date 2023/7/4 17:26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /**
     * 名称
     */
    private String name;
    /**
     * 年龄
     */
    private Integer age;
}
