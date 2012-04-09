package com.lucho.service;

import com.lucho.domain.User;

public interface UserService {

    void refreshFollowersFor(Integer ownerId);

    boolean shouldRefresh(User user);
}
