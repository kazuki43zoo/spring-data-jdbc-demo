package com.example.demo;

import java.sql.Clob;
import java.sql.SQLException;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.DataAccessStrategy;
import org.springframework.data.jdbc.core.DefaultDataAccessStrategy;
import org.springframework.data.jdbc.core.SqlGeneratorSource;
import org.springframework.data.jdbc.mapping.model.ConversionCustomizer;
import org.springframework.data.jdbc.mapping.model.JdbcMappingContext;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@SpringBootTest(classes = { SpringDataJdbcMybatisDemoApplication.class,
		SpringDataJdbcSpringJdbcImplTests.Config.class })
public class SpringDataJdbcSpringJdbcImplTests extends AbstractSpringDataJdbcTests {

	@EnableJdbcRepositories(repositoryImplementationPostfix = "SpringJdbcImpl")
	@Configuration
	static class Config {

		@Bean
		DataAccessStrategy dataAccessStrategy(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
				JdbcMappingContext context) {
			return new DefaultDataAccessStrategy(new SqlGeneratorSource(context), namedParameterJdbcTemplate, context);
		}

		@Bean
		ConversionCustomizer conversionCustomizer() {
			return conversionService -> {
				conversionService.addConverter(Clob.class, String.class, clob -> {
					try {
						return clob == null ? null : clob.getSubString(1L, (int) clob.length());
					} catch (SQLException e) {
						throw new IllegalStateException(e);
					}
				});
			};
		}
	}
}
