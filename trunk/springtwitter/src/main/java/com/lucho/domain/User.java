package com.lucho.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Entity;

/**
 * Created by IntelliJ IDEA.
 * User: lucianol
 * Date: 8/29/11
 * Time: 12:39 PM
 * To change this template use File | Settings | File Templates.
 */
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Entity
public class User {

}
