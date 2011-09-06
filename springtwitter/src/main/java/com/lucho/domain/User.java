package com.lucho.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validation.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;

@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Entity
@Table(name="t_user",
    uniqueConstraints = {@UniqueConstraint(columnNames={"username"})}
)
public class User implements UserDetails {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Integer id;

    @NotNull
    @NotEmpty
    @Size(max = 32)
    @Column(name = "username")
    private String username;

    @NotNull
    @NotEmpty
    @Size(min = 6, max = 32)
    @JsonIgnore
    private String password;

    @OneToMany
    @JsonIgnore
    private List<User> followedBy;

    @Transient
    @JsonIgnore
    private List<GrantedAuthority> authorities;

    @Transient
    private boolean beingFollowed;

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

    public boolean isBeingFollowed() {
        return beingFollowed;
    }

    public void setBeingFollowed(boolean beingFollowed) {
        this.beingFollowed = beingFollowed;
    }
}
