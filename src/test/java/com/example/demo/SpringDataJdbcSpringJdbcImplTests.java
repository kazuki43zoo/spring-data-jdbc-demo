package com.example.demo;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jdbc.core.DataAccessStrategy;
import org.springframework.data.jdbc.core.DefaultDataAccessStrategy;
import org.springframework.data.jdbc.core.SqlGeneratorSource;
import org.springframework.data.jdbc.mapping.model.ConversionCustomizer;
import org.springframework.data.jdbc.mapping.model.JdbcMappingContext;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import com.example.demo.domain.Todo;

@SpringBootTest(classes = { SpringDataJdbcMybatisDemoApplication.class,
		SpringDataJdbcSpringJdbcImplTests.SpringDataJdbcConfig.class })
public class SpringDataJdbcSpringJdbcImplTests extends AbstractSpringDataJdbcTests {

	@Test
	public void queryMethodForReturnTypeIsOptional() {
		Todo newTodo = new Todo();
		newTodo.setTitle("飲み会");
		newTodo.setDetails("銀座 19:00");
		super.todoRepository.save(newTodo);

		Optional<Todo> todo = super.todoRepository.findOptionalById(newTodo.getId());
		Assertions.assertThat(todo.isPresent()).isTrue();
		Assertions.assertThat(todo.get().getId()).isEqualTo(newTodo.getId());
		Assertions.assertThat(todo.get().getTitle()).isEqualTo(newTodo.getTitle());
		Assertions.assertThat(todo.get().getDetails()).isEqualTo(newTodo.getDetails());
		Assertions.assertThat(todo.get().isFinished()).isFalse();

		Assertions.assertThat(super.todoRepository.findOptionalById(9999)).isNotPresent();
	}

	@Test
	public void queryMethodForReturnTypeIsEntity() {
		Todo newTodo = new Todo();
		newTodo.setTitle("飲み会");
		newTodo.setDetails("銀座 19:00");
		super.todoRepository.save(newTodo);

		Todo todo = super.todoRepository.findEntityById(newTodo.getId());
		Assertions.assertThat(todo.getId()).isEqualTo(newTodo.getId());
		Assertions.assertThat(todo.getTitle()).isEqualTo(newTodo.getTitle());
		Assertions.assertThat(todo.getDetails()).isEqualTo(newTodo.getDetails());
		Assertions.assertThat(todo.isFinished()).isFalse();

		Assertions.assertThat(super.todoRepository.findEntityById(9999)).isNull();
	}

	@Test
	public void queryMethodForReturnTypeIsStream() {
		Todo newTodo = new Todo();
		newTodo.setTitle("飲み会");
		newTodo.setDetails("銀座 19:00");
		super.todoRepository.save(newTodo);

		List<Todo> todos = super.todoRepository.findAllStream().collect(Collectors.toList());
		Assertions.assertThat(todos).hasSize(1);
		Todo todo = todos.get(0);
		Assertions.assertThat(todo.getId()).isEqualTo(newTodo.getId());
		Assertions.assertThat(todo.getTitle()).isEqualTo(newTodo.getTitle());
		Assertions.assertThat(todo.getDetails()).isEqualTo(newTodo.getDetails());
		Assertions.assertThat(todo.isFinished()).isFalse();

		super.todoRepository.deleteAll();
		Assertions.assertThat(super.todoRepository.findAllStream()).isEmpty();
	}

	@Test
	public void queryMethodForReturnTypeIsList() {
		Todo newTodo = new Todo();
		newTodo.setTitle("飲み会");
		newTodo.setDetails("銀座 19:00");
		super.todoRepository.save(newTodo);

		List<Todo> todos = super.todoRepository.findAllList();
		Assertions.assertThat(todos).hasSize(1);
		Todo todo = todos.get(0);
		Assertions.assertThat(todo.getId()).isEqualTo(newTodo.getId());
		Assertions.assertThat(todo.getTitle()).isEqualTo(newTodo.getTitle());
		Assertions.assertThat(todo.getDetails()).isEqualTo(newTodo.getDetails());
		Assertions.assertThat(todo.isFinished()).isFalse();

		super.todoRepository.deleteAll();
		Assertions.assertThat(super.todoRepository.findAllList()).isEmpty();
	}

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
