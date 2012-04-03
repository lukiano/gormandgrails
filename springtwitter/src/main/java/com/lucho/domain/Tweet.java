package com.lucho.domain;

import com.lucho.util.LanguageDiscriminator;
import org.apache.solr.analysis.ASCIIFoldingFilterFactory;
import org.apache.solr.analysis.ISOLatin1AccentFilterFactory;
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

import javax.annotation.Nonnull;
import javax.persistence.*;
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
		tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
		filters = {
				@TokenFilterDef(factory = ASCIIFoldingFilterFactory.class),
				@TokenFilterDef(factory = LowerCaseFilterFactory.class),
				@TokenFilterDef(factory = SnowballPorterFilterFactory.class,
						params = {
								@Parameter(name = "language", value = "English")
						})
		}),
@AnalyzerDef(name = "es",
        tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
        filters = {
				@TokenFilterDef(factory = ASCIIFoldingFilterFactory.class),
                @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                @TokenFilterDef(factory = SnowballPorterFilterFactory.class,
                        params = {
                        @Parameter(name = "language", value = "Spanish")
                })
        })
})
public class Tweet implements Identifiable {

    private static final int MAX_TWEET_LENGTH = 140;

    @Id
    @GeneratedValue
    @JsonIgnore
	@DocumentId
    private Integer id;

    @NotNull
    @NotEmpty
    @Size(max = MAX_TWEET_LENGTH)
    @Field(index = Index.YES, store = Store.COMPRESS, termVector = TermVector.WITH_POSITION_OFFSETS)
    @Analyzer(definition = "da_analyzer")
    private String tweet;

	@Field
	@AnalyzerDiscriminator(impl = LanguageDiscriminator.class)
	private String language;

    @NotNull
    @ManyToOne
    private User owner;

    @NotNull
    @Past
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime creationDate;

    public DateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final DateTime theCreationDate) {
        this.creationDate = theCreationDate;
    }

    @Override
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
        || another instanceof Tweet && this.id.equals(((Tweet) another).id);
	}

	@Override
	public final int hashCode() {
		return this.id;
	}

}
