package com.example.demo.domain;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class Activity {
	@Id
	private int id;
	private String content;
	private LocalDateTime at;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getAt() {
		return at;
	}

	public void setAt(LocalDateTime at) {
		this.at = at;
	}
}
