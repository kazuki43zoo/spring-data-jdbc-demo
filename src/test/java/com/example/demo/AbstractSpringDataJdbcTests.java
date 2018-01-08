package com.example.demo;

import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Todo;
import com.example.demo.repository.TodoRepository;

@RunWith(SpringRunner.class)
@Transactional
public abstract class AbstractSpringDataJdbcTests {

	@Autowired
	private TodoRepository todoRepository;

	@Before
	public void cleanup() {
		todoRepository.deleteAll();
	}

	@Test
	public void insertAndFineById() {
		Todo newTodo = new Todo();
		newTodo.setTitle("飲み会");
		newTodo.setDetails("銀座 19:00");
		todoRepository.save(newTodo);

		Optional<Todo> todo = todoRepository.findById(newTodo.getId());
		Assertions.assertThat(todo.isPresent()).isTrue();
		Assertions.assertThat(todo.get().getId()).isEqualTo(newTodo.getId());
		Assertions.assertThat(todo.get().getTitle()).isEqualTo(newTodo.getTitle());
		Assertions.assertThat(todo.get().getDetails()).isEqualTo(newTodo.getDetails());
		Assertions.assertThat(todo.get().isFinished()).isFalse();
	}

	@Test
	public void update() {
		Todo newTodo = new Todo();
		newTodo.setTitle("飲み会");
		newTodo.setDetails("銀座 19:00");
		todoRepository.save(newTodo);

		newTodo.setTitle(newTodo.getTitle() + " Edit");
		newTodo.setDetails(newTodo.getDetails() + " Edit");
		newTodo.setFinished(true);
		todoRepository.save(newTodo);

		Optional<Todo> todo = todoRepository.findById(newTodo.getId());

		Assertions.assertThat(todo.isPresent()).isTrue();
		Assertions.assertThat(todo.get().getId()).isEqualTo(newTodo.getId());
		Assertions.assertThat(todo.get().getTitle()).isEqualTo(newTodo.getTitle());
		Assertions.assertThat(todo.get().getDetails()).isEqualTo(newTodo.getDetails());
		Assertions.assertThat(todo.get().isFinished()).isTrue();
	}

	@Test
	public void existsById() {
		Todo newTodo = new Todo();
		newTodo.setTitle("飲み会");
		newTodo.setDetails("銀座 19:00");
		todoRepository.save(newTodo);

		boolean exists = todoRepository.existsById(newTodo.getId());

		Assertions.assertThat(exists).isTrue();

		Assertions.assertThat(todoRepository.existsById(newTodo.getId() + 1)).isFalse();

	}

	@Test
	public void count() {
		Todo newTodo = new Todo();
		newTodo.setTitle("飲み会");
		newTodo.setDetails("銀座 19:00");
		todoRepository.save(newTodo);

		long count = todoRepository.count();

		Assertions.assertThat(count).isEqualTo(1);
	}

	@Test
	public void findAll() {
		Todo newTodo = new Todo();
		newTodo.setTitle("飲み会");
		newTodo.setDetails("銀座 19:00");
		todoRepository.save(newTodo);

		Iterator<Todo> todos = todoRepository.findAll().iterator();
		Assertions.assertThat(todos.hasNext()).isTrue();
		Todo todo = todos.next();
		Assertions.assertThat(todo.getId()).isEqualTo(newTodo.getId());
		Assertions.assertThat(todo.getTitle()).isEqualTo(newTodo.getTitle());
		Assertions.assertThat(todo.getDetails()).isEqualTo(newTodo.getDetails());
		Assertions.assertThat(todo.isFinished()).isFalse();
		Assertions.assertThat(todos.hasNext()).isFalse();
	}

	@Test
	public void findAllById() {
		Todo newTodo = new Todo();
		newTodo.setTitle("飲み会");
		newTodo.setDetails("銀座 19:00");
		todoRepository.save(newTodo);

		Iterator<Todo> todos = todoRepository.findAllById(Collections.singleton(newTodo.getId())).iterator();
		Assertions.assertThat(todos.hasNext()).isTrue();
		Todo todo = todos.next();
		Assertions.assertThat(todo.getId()).isEqualTo(newTodo.getId());
		Assertions.assertThat(todo.getTitle()).isEqualTo(newTodo.getTitle());
		Assertions.assertThat(todo.getDetails()).isEqualTo(newTodo.getDetails());
		Assertions.assertThat(todo.isFinished()).isFalse();
		Assertions.assertThat(todos.hasNext()).isFalse();
	}

	@Test
	public void delete() {
		Todo newTodo = new Todo();
		newTodo.setTitle("飲み会");
		newTodo.setDetails("銀座 19:00");
		todoRepository.save(newTodo);

		Assertions.assertThat(todoRepository.count()).isEqualTo(1);

		todoRepository.delete(newTodo);

		Assertions.assertThat(todoRepository.count()).isEqualTo(0);
	}

	@Test
	public void deleteById() {
		Todo newTodo = new Todo();
		newTodo.setTitle("飲み会");
		newTodo.setDetails("銀座 19:00");
		todoRepository.save(newTodo);

		Assertions.assertThat(todoRepository.count()).isEqualTo(1);

		todoRepository.deleteById(newTodo.getId());

		Assertions.assertThat(todoRepository.count()).isEqualTo(0);
	}

	@Test
	public void deleteSpecifiedEntities() {
		Todo newTodo = new Todo();
		newTodo.setTitle("飲み会");
		newTodo.setDetails("銀座 19:00");
		todoRepository.save(newTodo);

		Assertions.assertThat(todoRepository.count()).isEqualTo(1);

		todoRepository.deleteAll(Collections.singleton(newTodo));

		Assertions.assertThat(todoRepository.count()).isEqualTo(0);
	}

	@Test
	public void deleteAll() {
		Todo newTodo = new Todo();
		newTodo.setTitle("飲み会");
		newTodo.setDetails("銀座 19:00");
		todoRepository.save(newTodo);

		Assertions.assertThat(todoRepository.count()).isEqualTo(1);

		todoRepository.deleteAll();

		Assertions.assertThat(todoRepository.count()).isEqualTo(0);
	}

	@Test
	public void findAllByFinishedUsingCustomMethod() {
		Todo todo1 = new Todo();
		todo1.setTitle("ブレックファースト");
		todo1.setDetails("銀座 7:00");
		todoRepository.save(todo1);
		Todo todo2 = new Todo();
		todo2.setTitle("夜会");
		todo2.setDetails("銀座 3:00");
		todo2.setFinished(true);
		todoRepository.save(todo2);

		Iterator<Todo> todos = todoRepository.findAllByFinished(false).iterator();
		Assertions.assertThat(todos.hasNext()).isTrue();
		Todo todo = todos.next();
		Assertions.assertThat(todo.getId()).isEqualTo(todo1.getId());
		Assertions.assertThat(todo.getTitle()).isEqualTo(todo1.getTitle());
		Assertions.assertThat(todo.getDetails()).isEqualTo(todo1.getDetails());
		Assertions.assertThat(todo.isFinished()).isFalse();
		Assertions.assertThat(todos.hasNext()).isFalse();

		todos = todoRepository.findAllByFinished(true).iterator();
		Assertions.assertThat(todos.hasNext()).isTrue();
		todo = todos.next();
		Assertions.assertThat(todo.getId()).isEqualTo(todo2.getId());
		Assertions.assertThat(todo.getTitle()).isEqualTo(todo2.getTitle());
		Assertions.assertThat(todo.getDetails()).isEqualTo(todo2.getDetails());
		Assertions.assertThat(todo.isFinished()).isTrue();
		Assertions.assertThat(todos.hasNext()).isFalse();

	}

}
