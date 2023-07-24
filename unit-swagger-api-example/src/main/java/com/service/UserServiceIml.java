package com.service;

import com.bean.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sfh
 * @date 2023/7/4 17:28
 */
@Service
public class UserServiceIml extends A implements UserService {

    @Override
    public User get(String name) {
        return new User(name, null);
    }

    @Override
    public User get(List<String> name) {
        return new User(null, name.size());
    }

    @Override
    public User get(List<String> nameList1, List<Integer> nameList) {
        return new User(null, nameList.size());
    }

    @Override
    public User get(User user, String name) {
        user.setName(name);
        return user;
    }

    @Override
    public String a() {
        return super.a();
    }
}
