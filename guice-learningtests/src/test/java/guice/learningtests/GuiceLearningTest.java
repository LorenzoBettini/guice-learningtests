package guice.learningtests;

import com.google.inject.Inject;

public class GuiceLearningTest {

	private static class MyService {

	}

	private static class MyClient {
		MyService service;

		@Inject
		public MyClient(MyService service) {
			this.service = service;
		}
	}
}
