package com.lucho.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
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
@Table(name="t_user",
    uniqueConstraints = {@UniqueConstraint(columnNames={"username"})}
)
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    @NotBlank
    @Size(max = 32)
    @Column(name = "username")
    private String username;

    @NotNull
    @NotBlank
    @Size(min = 6, max = 32)
    private String password;

    @OneToMany
    @JsonIgnore
    private List<User> followedBy;

    @Transient
    private List<GrantedAuthority> authorities;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public String getPassword() {
        return password;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<User> getFollowedBy() {
        return followedBy;
    }

    public void setFollows(List<User> followedBy) {
        this.followedBy = followedBy;
    }

    public void setAuthorities(List<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}
