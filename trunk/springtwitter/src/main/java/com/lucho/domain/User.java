package com.lucho.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotEmpty;
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
public class User implements UserDetails, Identifiable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5788883283199993395L;
	
	private static final int MAX_USER_LENGTH = 32;
    private static final int MAX_PASSWORD_LENGTH = 32;
    private static final int MIN_PASSWORD_LENGTH = 6;

    @Id
    @GeneratedValue
    @JsonIgnore
    private Integer id;

    @NotNull
    @NotEmpty
    @Size(max = MAX_USER_LENGTH)
    @Column(name = "username")
    private String username;

    @NotNull
    @NotEmpty
    @Size(min = MIN_PASSWORD_LENGTH, max = MAX_PASSWORD_LENGTH)
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

    @Override
    @JsonIgnore
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
    @JsonIgnore
    public Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }


    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
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
