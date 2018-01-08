package com.example.demo;

import java.sql.Clob;
import java.sql.SQLException;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jdbc.core.DataAccessStrategy;
import org.springframework.data.jdbc.core.DefaultDataAccessStrategy;
import org.springframework.data.jdbc.core.SqlGeneratorSource;
import org.springframework.data.jdbc.mapping.model.ConversionCustomizer;
import org.springframework.data.jdbc.mapping.model.JdbcMappingContext;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

@SpringBootTest(classes = { SpringDataJdbcMybatisDemoApplication.class,
		SpringDataJdbcSpringJdbcImplTests.SpringDataJdbcConfig.class })
public class SpringDataJdbcSpringJdbcImplTests extends AbstractSpringDataJdbcTests {

	@EnableJdbcRepositories(repositoryImplementationPostfix = "SpringJdbcImpl")
	public static class SpringDataJdbcConfig {

		@Bean
		DataAccessStrategy dataAccessStrategy(NamedParameterJdbcOperations namedParameterJdbcOperations,
				JdbcMappingContext context) {
			return new DefaultDataAccessStrategy(new SqlGeneratorSource(context), namedParameterJdbcOperations,
					context);
		}

		@Bean
		ConversionCustomizer conversionCustomizer() {
			return conversionService -> {
				// for converter 'TEXT' column
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
