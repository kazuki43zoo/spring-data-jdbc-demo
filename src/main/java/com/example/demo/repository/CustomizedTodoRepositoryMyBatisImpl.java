package com.example.demo.repository;

import org.apache.ibatis.session.SqlSession;

import com.example.demo.domain.Todo;

public class CustomizedTodoRepositoryMyBatisImpl implements CustomizedTodoRepository {

	private final String NAMESPACE = Todo.class.getName() + "Mapper";
	private final SqlSession sqlSession;

	public CustomizedTodoRepositoryMyBatisImpl(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public Iterable<Todo> findAllByFinished(boolean finished) {
		return this.sqlSession.selectList(NAMESPACE + ".findAllByFinished", finished);
	}

}
