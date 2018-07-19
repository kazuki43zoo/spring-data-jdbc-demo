package com.example.demo;

import com.example.demo.domain.Todo;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;

import java.sql.Clob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//import org.springframework.data.relational.core.mapping.ConversionCustomizer;

@SpringBootTest(classes = {SpringDataJdbcMybatisDemoApplication.class,
		SpringDataJdbcSpringJdbcImplTests.SpringDataJdbcConfig.class})
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

	@Test
	public void queryMethodForReturnTypeIsLong() {
		Todo newTodo = new Todo();
		newTodo.setTitle("飲み会");
		newTodo.setDetails("銀座 19:00");
		super.todoRepository.save(newTodo);

		Assertions.assertThat(super.todoRepository.countByFinished(false)).isEqualTo(1);
		Assertions.assertThat(super.todoRepository.countByFinished(true)).isEqualTo(0);
	}

	@Test
	public void queryMethodForReturnTypeIsBoolean() {
		Todo newTodo = new Todo();
		newTodo.setTitle("飲み会");
		newTodo.setDetails("銀座 19:00");
		super.todoRepository.save(newTodo);

		Assertions.assertThat(super.todoRepository.existsByFinished(false)).isTrue();
		Assertions.assertThat(super.todoRepository.existsByFinished(true)).isFalse();
	}

	@Test
	public void queryMethodForReturnTypeIsLocalDateTime() {
		Assertions.assertThat(super.todoRepository.currentDateTime()).isBeforeOrEqualTo(LocalDateTime.now());
	}

	@Test
	public void modifyingQuery() {
		Todo newTodo = new Todo();
		newTodo.setTitle("飲み会");
		newTodo.setDetails("銀座 19:00");
		super.todoRepository.save(newTodo);
		Assertions.assertThat(super.todoRepository.updateFinishedById(newTodo.getId(), true)).isTrue();
		Assertions.assertThat(super.todoRepository.findById(newTodo.getId()).get().isFinished()).isTrue();

	}

	@EnableJdbcAuditing
	@EnableJdbcRepositories(repositoryImplementationPostfix = "SpringJdbcImpl")
	public static class SpringDataJdbcConfig {

		@Bean
		CustomConversions jdbcCustomConversions() {
			return new JdbcCustomConversions(Collections.singletonList(new Converter<Clob, String>() {
				@Override
				public String convert(Clob clob) {
					try {
						return clob == null ? null : clob.getSubString(1L, (int) clob.length());
					} catch (SQLException e) {
						throw new IllegalStateException(e);
					}
				}
			}));
		}

		@Bean
		NamingStrategy namingStrategy() {
			return new NamingStrategy() {
				@Override
				public String getReverseColumnName(RelationalPersistentProperty property) {
					return NamingStrategy.super.getReverseColumnName(property).toLowerCase() + "_id";
				}

				@Override
				public String getKeyColumn(RelationalPersistentProperty property) {
					return "sort_order";
				}
			};
		}

		@Bean
		AuditorAware<String> auditorAware() {
			return new MyAuditorAware();
		}

	}

}
