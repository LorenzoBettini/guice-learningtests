package guice.learningtests;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;

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

	@Test
	public void canInstantiateConcreteClassesWithoutConfiguration() {
		Module module = new AbstractModule() {};
		Injector injector = Guice.createInjector(module);
		MyClient client = injector.getInstance(MyClient.class);
		assertNotNull(client.service);
	}
}
