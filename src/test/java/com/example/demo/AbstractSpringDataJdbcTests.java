package com.example.demo;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;

import com.example.demo.domain.Activity;
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
	protected TodoRepository todoRepository;

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
	public void saveAll() {
		Todo todo1 = new Todo();
		todo1.setTitle("飲み会");
		todo1.setDetails("銀座 19:00");

		Todo todo2 = new Todo();
		todo2.setTitle("ランチ");
		todo2.setDetails("銀座 12:30");
		todo2.setFinished(true);

		todoRepository.saveAll(Arrays.asList(todo1, todo2));

		Assertions.assertThat(todoRepository.count()).isEqualTo(2);

		Optional<Todo> todo = todoRepository.findById(todo1.getId());
		Assertions.assertThat(todo.isPresent()).isTrue();
		Assertions.assertThat(todo.get().getId()).isEqualTo(todo1.getId());
		Assertions.assertThat(todo.get().getTitle()).isEqualTo(todo1.getTitle());
		Assertions.assertThat(todo.get().getDetails()).isEqualTo(todo1.getDetails());
		Assertions.assertThat(todo.get().isFinished()).isFalse();

		todo = todoRepository.findById(todo2.getId());
		Assertions.assertThat(todo.isPresent()).isTrue();
		Assertions.assertThat(todo.get().getId()).isEqualTo(todo2.getId());
		Assertions.assertThat(todo.get().getTitle()).isEqualTo(todo2.getTitle());
		Assertions.assertThat(todo.get().getDetails()).isEqualTo(todo2.getDetails());
		Assertions.assertThat(todo.get().isFinished()).isTrue();

		todo1.setFinished(true);
		todo2.setFinished(false);

		todoRepository.saveAll(Arrays.asList(todo1, todo2));

		todo = todoRepository.findById(todo1.getId());
		Assertions.assertThat(todo.isPresent()).isTrue();
		Assertions.assertThat(todo.get().isFinished()).isTrue();

		todo = todoRepository.findById(todo2.getId());
		Assertions.assertThat(todo.isPresent()).isTrue();
		Assertions.assertThat(todo.get().isFinished()).isFalse();

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

@Test
public void oneToMany() {

	Todo newTodo = new Todo();
	newTodo.setTitle("飲み会");
	newTodo.setDetails("銀座 19:00");
	Activity activity1 = new Activity();
	activity1.setContent("Created");
	activity1.setAt(LocalDateTime.now());

	Activity activity2 = new Activity();
	activity2.setContent("Started");
	activity2.setAt(LocalDateTime.now());

	newTodo.setActivities(Arrays.asList(activity1, activity2));

	// Insert
	todoRepository.save(newTodo);

	// Assert for inserting
	Optional<Todo> loadedTodo = todoRepository.findById(newTodo.getId());
	Assertions.assertThat(loadedTodo.isPresent()).isTrue();
	loadedTodo.ifPresent(todo -> {
		Assertions.assertThat(todo.getId()).isEqualTo(newTodo.getId());
		Assertions.assertThat(todo.getTitle()).isEqualTo(newTodo.getTitle());
		Assertions.assertThat(todo.getDetails()).isEqualTo(newTodo.getDetails());
		Assertions.assertThat(todo.isFinished()).isFalse();
		Assertions.assertThat(todo.getActivities()).hasSize(2);
		Assertions.assertThat(todo.getActivities().get(0).getContent()).isEqualTo(activity1.getContent());
		Assertions.assertThat(todo.getActivities().get(1).getContent()).isEqualTo(activity2.getContent());

	});

	Activity activity3 = new Activity();
	activity3.setContent("Changed Title");
	activity3.setAt(LocalDateTime.now());

	loadedTodo.ifPresent(todo -> {
		todo.setTitle("[Change] " + todo.getTitle());
		todo.getActivities().add(activity3);
	});

	// Update & (Delete & Insert)
	todoRepository.save(loadedTodo.get());
	loadedTodo = todoRepository.findById(newTodo.getId());

	// Assert for updating
	Assertions.assertThat(loadedTodo.isPresent()).isTrue();
	loadedTodo.ifPresent(todo -> {
		Assertions.assertThat(todo.getTitle()).isEqualTo("[Change] " + newTodo.getTitle());
		Assertions.assertThat(todo.getActivities()).hasSize(3);
		Assertions.assertThat(todo.getActivities().get(0).getContent()).isEqualTo(activity1.getContent());
		Assertions.assertThat(todo.getActivities().get(1).getContent()).isEqualTo(activity2.getContent());
		Assertions.assertThat(todo.getActivities().get(2).getContent()).isEqualTo(activity3.getContent());
	});

	// Delete
	todoRepository.deleteById(newTodo.getId());

	// Assert for deleting
	Assertions.assertThat(todoRepository.findById(newTodo.getId())).isNotPresent();

}

}
