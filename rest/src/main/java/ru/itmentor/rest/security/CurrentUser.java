package ru.itmentor.rest.security;

import org.springframework.security.core.authority.AuthorityUtils;
import ru.itmentor.common.entity.User;

public class CurrentUser extends org.springframework.security.core.userdetails.User {

    private User user;

    public CurrentUser(User user) {
        super(user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRoleNames()));
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}

