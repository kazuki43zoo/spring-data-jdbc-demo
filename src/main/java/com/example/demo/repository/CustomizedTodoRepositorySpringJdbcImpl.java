package com.example.demo.repository;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import com.example.demo.domain.Todo;

public class CustomizedTodoRepositorySpringJdbcImpl implements CustomizedTodoRepository {

	private static final RowMapper<Todo> ROW_MAPPER = new BeanPropertyRowMapper<>(Todo.class);

	private final NamedParameterJdbcOperations namedParameterJdbcOperations;

	public CustomizedTodoRepositorySpringJdbcImpl(NamedParameterJdbcOperations namedParameterJdbcOperations) {
		this.namedParameterJdbcOperations = namedParameterJdbcOperations;
	}

	public Iterable<Todo> findAllByFinished(boolean finished) {
		return this.namedParameterJdbcOperations.query(
				"SELECT id, title, details, finished FROM todo WHERE finished = :finished ORDER BY id",
				new MapSqlParameterSource("finished", finished), ROW_MAPPER);
	}

}
