package com.lucho.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: lucianol
 * Date: 8/29/11
 * Time: 12:39 PM
 * To change this template use File | Settings | File Templates.
 */
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Entity
@Table(name="user",
    uniqueConstraints = {@UniqueConstraint(columnNames={"nickname"})}
)
public class User {

    @Id
    private Integer id;

    @NotNull
    @Size(max = 32)
    @Column(name = "nickname", unique=true)
    private String nickname;

    @NotNull
    @Size(min = 6, max = 32)
    private String password;

    @OneToMany
    private List<User> follows;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<User> getFollows() {
        return follows;
    }

    public void setFollows(List<User> follows) {
        this.follows = follows;
    }
}
