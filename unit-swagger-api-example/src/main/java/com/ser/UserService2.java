package com.ser;

import com.bean.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sfh
 * @date 2023/7/4 17:28
 */
@Service
public class UserService2 {

    public User get(String name) {
        return new User(name, null);
    }

    public User get(List<String> name) {
        return new User(null, name.size());
    }
    public User get(List<List<List<Integer>>> nameList1, List<String> nameList) {
        return new User(null, nameList.size());
    }

    public User get(User user, String name) {
        user.setName(name);
        return user;
    }
}
