package com.justanalytics.publishapi;

import org.junit.jupiter.api.Test;

// @SpringBootTest
class PublishapiApplicationTests {

	static {
		System.setProperty("CERTIFICATE_THUMBPRINT", "E4E8CEE6E2408B1295471DB236DFFC9680279A5D");
		System.setProperty("CERTIFICATE_HEADER", "X-ARR-ClientCert");
	}

	@Test
	void contextLoads() {
	}

}
