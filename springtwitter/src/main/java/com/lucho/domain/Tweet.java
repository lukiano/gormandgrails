package com.lucho.domain;

import org.apache.solr.analysis.LowerCaseFilterFactory;
import org.apache.solr.analysis.SnowballPorterFilterFactory;
import org.apache.solr.analysis.StandardTokenizerFactory;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

@Entity
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Indexed
@Table(name="t_tweet")
@AnalyzerDef(name = "da_analyzer",
        tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
        filters = {
                @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                @TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = {
                        @Parameter(name = "language", value = "Spanish")
                })
        })

public final class Tweet implements Identifiable {

    private static final int MAX_TWEET_LENGTH = 140;

    @Id
    @GeneratedValue
    @JsonIgnore
    private Integer id;

    @NotNull
    @NotEmpty
    @Size(max = MAX_TWEET_LENGTH)
    @Field(index = Index.TOKENIZED, store = Store.NO)
    @Analyzer(definition = "da_analyzer")
    private String tweet;

    @NotNull
    @ManyToOne
    private User owner;

    @NotNull
    @Past
    @Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime creationDate;

    public DateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final DateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(final String tweet) {
        this.tweet = tweet;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(final User owner) {
        this.owner = owner;
    }
}
