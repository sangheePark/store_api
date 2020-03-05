package co.kr.snack.store.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 
 * <b></b>
 * <pre>
 * <b>Description:</b>
 * </pre>
 *
 * <pre>
 * <b>History:</b>
 * - 2020.02.06, snack: 최초작성 
 * </pre>
 * @author snack (novles@naver.com)
 * @Version 1.0, 2020.02.06
 */
//@Configuration
//@MapperScan(basePackages = "net.spotv.adserver.mapper")
//@EnableTransactionManagement
public class DatabaseConfig {
	
    @Bean
    public  SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setConfigLocation (new PathMatchingResourcePatternResolver().getResource("classpath:/mybatis-config.xml"));
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/**/*.xml"));
        sessionFactory.setConfigurationProperties(sqlSessionFactoryProperties());
        sessionFactory.setTypeHandlersPackage("net.spotv.adserver.enums");
        return sessionFactory.getObject();
    }
    
    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
      final SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
      return sqlSessionTemplate;
    }
	
	@Bean
	public Properties sqlSessionFactoryProperties() {
		Properties properties = new Properties();
		properties.setProperty("cacheEnabled", "false");
		properties.setProperty("lazyLoadingEnabled", "false");
		properties.setProperty("jdbcTypeForNull", "NULL");
		properties.setProperty("localCacheScope", "STATEMENT");
		return properties;
	}
}
