package com.example.demo;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jdbc.core.DataAccessStrategy;
import org.springframework.data.jdbc.mybatis.MyBatisDataAccessStrategy;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@SpringBootTest(classes = { SpringDataJdbcMybatisDemoApplication.class, SpringDataJdbcMyBatisImplTests.Config.class })
public class SpringDataJdbcMyBatisImplTests extends AbstractSpringDataJdbcTests {

	@EnableJdbcRepositories(repositoryImplementationPostfix = "MyBatisImpl")
	public static class Config {
		@Bean
		DataAccessStrategy dataAccessStrategy(SqlSessionFactory sqlSessionFactory) {
			return new MyBatisDataAccessStrategy(sqlSessionFactory);
		}
	}

}
