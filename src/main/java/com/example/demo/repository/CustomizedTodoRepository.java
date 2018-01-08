package com.example.demo.repository;

import com.example.demo.domain.Todo;

public interface CustomizedTodoRepository {

	Iterable<Todo> findAllByFinished(boolean finished);

}
