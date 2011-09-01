package com.lucho.domain;

import org.apache.solr.analysis.LowerCaseFilterFactory;
import org.apache.solr.analysis.SnowballPorterFilterFactory;
import org.apache.solr.analysis.StandardTokenizerFactory;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.*;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: lucianol
 * Date: 8/30/11
 * Time: 9:36 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Indexed
@AnalyzerDef(name = "da_analyzer",
        tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
        filters = {
                @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                @TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = {
                        @Parameter(name = "language", value = "Spanish")
                })
        })

public class Tweet {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    @NotBlank
    @Size(max = 140)
    @Field(index = Index.TOKENIZED, store = Store.NO)
    @Analyzer(definition = "da_analyzer")
    private String tweet;

    @NotNull
    @ManyToOne
    private User owner;

    @NotNull
    @NotBlank
    private Date creationDate;

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
