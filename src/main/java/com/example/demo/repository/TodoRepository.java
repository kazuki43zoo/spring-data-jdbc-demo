package com.example.demo.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.domain.Todo;

public interface TodoRepository extends CrudRepository<Todo, Integer>, CustomizedTodoRepository {

	@Query("SELECT * FROM todo WHERE id = :id")
	Optional<Todo> findOptionalById(@Param("id") Integer id);

	@Query("SELECT * FROM todo WHERE id = :id")
	Todo findEntityById(@Param("id") Integer id);

	@Query("SELECT * FROM todo ORDER BY id")
	Stream<Todo> findAllStream();

	@Query("SELECT * FROM todo ORDER BY id")
	List<Todo> findAllList();

}
