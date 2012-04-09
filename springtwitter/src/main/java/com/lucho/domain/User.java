package com.lucho.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.Nonnull;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;

/**
 * Represents a user of the system.
 */
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Entity
@Cacheable
@Table(name = "t_user",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})}
)
public class User implements UserDetails {

    /**
     * Unique identifier for serialization purposes.
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

    @ManyToMany
    @JsonIgnore
    private List<User> followedBy;

    @Transient
    @JsonIgnore
    private List<GrantedAuthority> authorities;

    @Transient
    private boolean canFollow;

    protected User() {}

    public User(final String name, final String pass) {
        this.username = name;
        this.password = pass;
    }

    @JsonIgnore
    @Nonnull
    public final Integer getId() {
        return id;
    }

    @Override
    public final String getUsername() {
        return username;
    }

    public final void setUsername(final String anUsername) {
        this.username = anUsername;
    }

    @Override
    @JsonIgnore
    public final Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @JsonIgnore
    public final String getPassword() {
        return password;
    }


    @Override
    @JsonIgnore
    public final boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public final boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public final boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public final boolean isEnabled() {
        return true;
    }

    public final void setPassword(final String password) {
        this.password = password;
    }

    @JsonIgnore
    public final List<User> getFollowedBy() {
        return followedBy;
    }

    public final void setFollowedBy(final List<User> followers) {
        this.followedBy = followers;
    }

    public final void setAuthorities(
            final List<GrantedAuthority> theAuthorities) {
        this.authorities = theAuthorities;
    }

    public final boolean canFollow() {
        return canFollow;
    }

    public final void setCanFollow(final boolean canIFollow) {
        this.canFollow = canIFollow;
    }

    @Override
    public final boolean equals(final Object another) {
        return (another == this)
                || another instanceof User
                && this.id.equals(((User) another).id);
    }

    @Override
    public final int hashCode() {
        return this.id;
    }

}
