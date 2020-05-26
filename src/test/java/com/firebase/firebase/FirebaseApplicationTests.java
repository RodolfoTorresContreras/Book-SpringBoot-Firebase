package com.firebase.firebase;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FirebaseApplicationTests {
        @Autowired
        UserController controller;
	@Test
	void contextLoads() {
            Assertions.assertNotNull(controller);
	}

}
