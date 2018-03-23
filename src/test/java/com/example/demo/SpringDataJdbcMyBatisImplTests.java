package com.example.demo;

import org.apache.ibatis.session.SqlSession;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jdbc.core.DataAccessStrategy;
import org.springframework.data.jdbc.mapping.model.JdbcPersistentProperty;
import org.springframework.data.jdbc.mapping.model.NamingStrategy;
import org.springframework.data.jdbc.mybatis.MyBatisDataAccessStrategy;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@SpringBootTest(classes = { SpringDataJdbcMybatisDemoApplication.class,
		SpringDataJdbcMyBatisImplTests.SpringDataJdbcConfig.class })
public class SpringDataJdbcMyBatisImplTests extends AbstractSpringDataJdbcTests {

	@EnableJdbcRepositories(repositoryImplementationPostfix = "MyBatisImpl")
	public static class SpringDataJdbcConfig {
		@Bean
		DataAccessStrategy dataAccessStrategy(SqlSession sqlSession) {
			return new MyBatisDataAccessStrategy(sqlSession);
		}

		@Bean
		NamingStrategy namingStrategy() {
			return new NamingStrategy(){
				@Override
				public String getReverseColumnName(JdbcPersistentProperty property) {
					return NamingStrategy.super.getReverseColumnName(property).toLowerCase() + "_id";
				}
				@Override
				public String getKeyColumn(JdbcPersistentProperty property) {
					return "sort_order";
				}
			};
		}
	}

}
