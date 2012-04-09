package com.lucho.domain;

import com.lucho.util.LanguageDiscriminator;
import org.apache.solr.analysis.ASCIIFoldingFilterFactory;
import org.apache.solr.analysis.LowerCaseFilterFactory;
import org.apache.solr.analysis.SnowballPorterFilterFactory;
import org.apache.solr.analysis.StandardFilterFactory;
import org.apache.solr.analysis.StandardTokenizerFactory;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.AnalyzerDefs;
import org.hibernate.search.annotations.AnalyzerDiscriminator;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.TermVector;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import javax.annotation.Nonnull;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

/**
 * Domain object that represents a tweet an user wrote.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Indexed
@Cacheable
@Table(name = "t_tweet")
@AnalyzerDefs({
        @AnalyzerDef(name = "en",
                tokenizer = @TokenizerDef(factory =
                        StandardTokenizerFactory.class),
                filters = {
                        @TokenFilterDef(factory =
                                StandardFilterFactory.class),
                        @TokenFilterDef(factory =
                                ASCIIFoldingFilterFactory.class),
                        @TokenFilterDef(factory =
                                LowerCaseFilterFactory.class),
                        @TokenFilterDef(factory =
                                SnowballPorterFilterFactory.class,
                                params = {
                                        @Parameter(name = "language",
                                                value = "English")
                                })
                }),
        @AnalyzerDef(name = "es",
                tokenizer = @TokenizerDef(factory =
                        StandardTokenizerFactory.class),
                filters = {
                        @TokenFilterDef(factory =
                                StandardFilterFactory.class),
                        @TokenFilterDef(factory =
                                ASCIIFoldingFilterFactory.class),
                        @TokenFilterDef(factory =
                                LowerCaseFilterFactory.class),
                        @TokenFilterDef(factory =
                                SnowballPorterFilterFactory.class,
                                params = {
                                        @Parameter(name = "language",
                                                value = "Spanish")
                                })
                })
})
public class Tweet {

    /**
     * Maximum length for a tweet text.
     */
    private static final int MAX_TWEET_LENGTH = 140;

    /**
     * Tweet id.
     */
    @Id
    @GeneratedValue
    @JsonIgnore
    @DocumentId
    private Integer id;

    /**
     * Tweet text.
     */
    @NotNull
    @NotEmpty
    @Size(max = MAX_TWEET_LENGTH)
    @Field(index = Index.YES, store = Store.COMPRESS,
            termVector = TermVector.WITH_POSITION_OFFSETS)
    private String tweet;

    /**
     * Tweet language.
     */
    @Field
    @AnalyzerDiscriminator(impl = LanguageDiscriminator.class)
    private String language;

    /**
     * Tweet owner.
     */
    @NotNull
    @ManyToOne
    private User owner;

    @NotNull
    @Past
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime creationDate;

    /**
     * Default Class Constructor.
     */
    protected Tweet() {

    }

    /**
     * Creates a new Tweet filling the obligatory fields.
     * @param user Tweet owner.
     * @param text Tweet contents.
     * @param lang Tweet language.
     */
    public Tweet(final User user, final String text, final String lang) {
        this.owner = user;
        this.tweet = text;
        this.language = lang;
        this.creationDate = new DateTime();
    }

    public DateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final DateTime theCreationDate) {
        this.creationDate = theCreationDate;
    }

    @Nonnull
    public final Integer getId() {
        return id;
    }

    public final void setId(final Integer anId) {
        this.id = anId;
    }

    public final String getTweet() {
        return tweet;
    }

    public final void setTweet(final String aTweet) {
        this.tweet = aTweet;
    }

    public final User getOwner() {
        return owner;
    }

    public final void setOwner(final User newOwner) {
        this.owner = newOwner;
    }

    public final String getLanguage() {
        return language;
    }

    public final void setLanguage(final String newLanguage) {
        this.language = newLanguage;
    }

    @Override
    public final boolean equals(final Object another) {
        return (another == this)
                || another instanceof Tweet
                && this.id.equals(((Tweet) another).id);
    }

    @Override
    public final int hashCode() {
        return this.id;
    }

}
