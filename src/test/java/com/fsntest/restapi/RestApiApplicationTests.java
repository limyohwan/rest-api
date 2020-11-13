package com.fsntest.restapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootTest
@ComponentScan({"com.fsntest.restapi.controller.v1"})
@EntityScan({"com.fsntest.restapi.entity"})
@EnableJpaRepositories({"com.fsntest.restapi.repo"})
class RestApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
