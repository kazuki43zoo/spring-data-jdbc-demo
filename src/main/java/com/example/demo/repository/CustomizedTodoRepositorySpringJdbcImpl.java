package com.example.demo.repository;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.example.demo.domain.Todo;

public class CustomizedTodoRepositorySpringJdbcImpl implements CustomizedTodoRepository {

	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public CustomizedTodoRepositorySpringJdbcImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public Iterable<Todo> findAllByFinished(boolean finished) {
		return this.namedParameterJdbcTemplate.query(
				"SELECT id, title, details, finished FROM todo WHERE finished = :finished ORDER BY id",
				new MapSqlParameterSource("finished", finished), new BeanPropertyRowMapper<Todo>(Todo.class));
	}

}
