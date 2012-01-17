package com.lucho.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.infinispan.loaders.CacheLoaderException;
import org.infinispan.loaders.jdbc.connectionfactory.ConnectionFactory;
import org.infinispan.loaders.jdbc.connectionfactory.ConnectionFactoryConfig;
import org.infinispan.loaders.jdbc.logging.Log;
import org.infinispan.util.logging.LogFactory;

public class SpringManagedConnectionFactory extends ConnectionFactory {

	private static final Log log = LogFactory.getLog(SpringManagedConnectionFactory.class, Log.class);
	private static final boolean trace = log.isTraceEnabled();

	private DataSource dataSource;

	public void start(ConnectionFactoryConfig config, ClassLoader classLoader) throws CacheLoaderException {
		InitialContext ctx = null;
		String datasourceName = config.getDatasourceJndiLocation();
		try {
			ctx = new InitialContext();
			dataSource = (DataSource) ctx.lookup(datasourceName);
			if (trace) {
				log.tracef("Datasource lookup for %s succeeded: %b", datasourceName, dataSource);
			}
			if (dataSource == null) {
				log.connectionInJndiNotFound(datasourceName);
				throw new CacheLoaderException(String.format("Could not find a connection in jndi under the name '%s'",
						datasourceName));
			}
		} catch (NamingException e) {
			log.namingExceptionLookingUpConnection(datasourceName, e);
			throw new CacheLoaderException(e);
		} finally {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (NamingException e) {
					log.failedClosingNamingCtx(e);
				}
			}
		}
	}

	public void stop() {
	}

	public Connection getConnection() throws CacheLoaderException {
		Connection connection;
		try {
			connection = dataSource.getConnection();
		} catch (SQLException e) {
			log.sqlFailureRetrievingConnection(e);
			throw new CacheLoaderException("This might be related to https://jira.jboss.org/browse/ISPN-604", e);
		}
		if (trace) {
			log.tracef("Connection checked out: %s", connection);
		}
		return connection;

	}

	public void releaseConnection(Connection conn) {
		try {
			if (conn != null) // Could be null if getConnection failed
				conn.close();
		} catch (SQLException e) {
			log.sqlFailureClosingConnection(conn, e);
		}
	}

}
