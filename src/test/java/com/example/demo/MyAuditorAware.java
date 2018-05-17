package com.example.demo;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class MyAuditorAware implements AuditorAware<String> {

	static ThreadLocal<String> currentUser = ThreadLocal.withInitial(() -> "default");

	public Optional<String> getCurrentAuditor() {
		return Optional.ofNullable(currentUser.get());
	}

}
