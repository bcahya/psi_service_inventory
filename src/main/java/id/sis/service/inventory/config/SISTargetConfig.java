package id.sis.service.inventory.config;

import javax.sql.DataSource;

import org.postgresql.xa.PGXADataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.atomikos.jdbc.AtomikosDataSourceBean;

import id.sis.service.inventory.properties.SISTargetDataSourceProperties;

@Configuration
@EnableConfigurationProperties(SISTargetDataSourceProperties.class)
public class SISTargetConfig {
    @Autowired
    SISTargetDataSourceProperties sisTargetDataSourceProperties;

    @Bean(name = "sisTargetDataSource")
    public DataSource sisDataSource() {
    	PGXADataSource pgXADataSource = new PGXADataSource();
		 pgXADataSource.setUrl(sisTargetDataSourceProperties.getUrl());
		 pgXADataSource.setPassword(sisTargetDataSourceProperties.getPassword());
		 pgXADataSource.setUser(sisTargetDataSourceProperties.getUsername());
	
		 AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
		 xaDataSource.setXaDataSource(pgXADataSource);
		 xaDataSource.setUniqueResourceName("xads2");
		 xaDataSource.setMaxPoolSize(Integer.valueOf(sisTargetDataSourceProperties.getMaxpool()));
		 return xaDataSource;
    }

    @Bean(name = "jdbcTemplateTarget")
    public JdbcTemplate jdbcTemplate(@Qualifier(value = "sisTargetDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
