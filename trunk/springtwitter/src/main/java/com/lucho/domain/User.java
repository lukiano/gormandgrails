package com.lucho.domain;

import com.lucho.repository.TweetRepository;
import com.lucho.repository.UserRepository;
import com.lucho.service.UserService;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.inject.Inject;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a user of the system.
 */
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Entity
@Cacheable
@Table(name = "t_user",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})}
)
@Configurable
public class User implements UserDetails {

    /**
     * TweetRepository implementation.
     */
    @Inject
    @Transient
    @JsonIgnore
    private transient TweetRepository tweetRepository;

    /**
     * TweetRepository implementation.
     */
    @Inject
    @Transient
    @JsonIgnore
    private transient UserRepository userRepository;

    /**
     * User service.
     */
    @Inject
    @Transient
    @JsonIgnore
    private transient UserService userService;


    /**
     * Unique identifier for serialization purposes.
     */
    private static final long serialVersionUID = 5788883283199993395L;

    private static final int MAX_USER_LENGTH = 32;
    private static final int MAX_PASSWORD_LENGTH = 32;
    private static final int MIN_PASSWORD_LENGTH = 6;

    @Id
    @GeneratedValue
    private Integer id;

    @NotEmpty
    @Size(max = MAX_USER_LENGTH)
    @Column(name = "username")
    private String username;

    @NotEmpty
    @Size(min = MIN_PASSWORD_LENGTH, max = MAX_PASSWORD_LENGTH)
    @JsonIgnore
    private String password;

    @ManyToMany
    @JsonIgnore
    private Set<User> followedBy;

    @Transient
    @JsonIgnore
    private List<GrantedAuthority> authorities;

    @Transient
    @JsonProperty
    private boolean canFollow;

    /**
     * Default Class Constructor.
     */
    protected User() {

    }

    /**
     * Creates a new User filling the obligatory fields.
     *
     * @param name User name.
     * @param pass User password.
     */
    public User(final String name, final String pass) {
        this.username = name;
        this.password = pass;
        this.followedBy = new HashSet<User>();
        this.followedBy.add(this);
    }

    public final Integer getId() {
        return id;
    }

    @Override
    public final String getUsername() {
        return username;
    }

    @Override
    @JsonIgnore
    public final Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @JsonIgnore
    @Override
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

    @JsonIgnore
    public final Set<User> getFollowedBy() {
        return followedBy;
    }

    public final void setAuthorities(
            final List<GrantedAuthority> theAuthorities) {
        this.authorities = theAuthorities;
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

    @JsonIgnore
    public final void save() {
        this.userRepository.save(this);
    }

    @JsonIgnore
    public final Boolean followUser(final User userToFollow) {
        return this.userRepository.followUser(this, userToFollow);
    }

    @JsonIgnore
    public final List<Tweet> getTweetsIncludingFollows(final Long millis) {
        return this.tweetRepository.getTweetsForUserIncludingFollows(
                this, millis);
    }

    @JsonIgnore
    public final Boolean shouldRefresh() {
        return this.userService.shouldRefresh(this);
    }

    @Override
    public final String toString() {
        return this.username;
    }
}
