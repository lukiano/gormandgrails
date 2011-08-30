package com.lucho.dao;

import com.lucho.domain.Tweet;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: lucianol
 * Date: 8/30/11
 * Time: 10:18 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class TweetDao extends HibernateDaoSupport {

    public List<Tweet> getTweetsForUser(final String nickname) {
        return null;
    }

    public List<Tweet> getTweetsForUserIncludingFollows(final String nickname) {
        return null;
    }

}
