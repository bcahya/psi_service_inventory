package id.sis.service.inventory.config;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import id.sis.service.inventory.properties.SISTargetDataSourceProperties;

@Configuration
public class SISTargetConfig {

    @Bean(name = "targetDataSource")
    public DataSource targetDataSource(SISTargetDataSourceProperties prop) {

        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(prop.getUrl());
        ds.setUsername(prop.getUsername());
        ds.setPassword(prop.getPassword());

        ds.setMaximumPoolSize(prop.getMaxPoolSize());
        ds.setMinimumIdle(prop.getMinIdle());
        ds.setConnectionTimeout(prop.getConnectionTimeout());
        ds.setIdleTimeout(prop.getIdleTimeout());
        ds.setMaxLifetime(prop.getMaxLifetime());

        ds.setAutoCommit(false); // PENTING untuk @Transactional

        return ds;
    }

    @Bean(name = "jdbcTemplateTarget")
    public JdbcTemplate jdbcTemplateTarget(
            @Qualifier("targetDataSource") DataSource ds) {
        return new JdbcTemplate(ds);
    }

    @Bean(name = "targetTxManager")
    public PlatformTransactionManager targetTxManager(
            @Qualifier("targetDataSource") DataSource ds) {
        return new DataSourceTransactionManager(ds);
    }
}