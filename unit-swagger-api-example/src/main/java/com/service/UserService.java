package com.service;

import com.bean.User;

import java.util.List;

/**
 * @author sfh
 * @date 2023/7/14 11:30
 */
public interface UserService {
     User get(String name);

     User get(List<String> name);
     User get(List<String> nameList1, List<Integer> nameList);
     User get(User user, String name);
}
