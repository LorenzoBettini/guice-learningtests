package guice.learningtests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Singleton;

public class GuiceSingletonLearningTest {

	private static interface IMyService {

	}

	private static class MyService implements IMyService {

	}

	private static class MyClient {
		IMyService service;

		@Inject
		public MyClient(IMyService service) {
			this.service = service;
		}
	}

	@Test
	public void bindToSingleton() {
		Module module = new AbstractModule() {
			@Override
			protected void configure() {
				bind(IMyService.class).to(MyService.class);
				bind(MyService.class).in(Singleton.class);
			}
		};
		Injector injector = Guice.createInjector(module);
		MyClient client1 = injector.getInstance(MyClient.class);
		MyClient client2 = injector.getInstance(MyClient.class);
		assertNotNull(client1.service);
		assertSame(client1.service, client2.service);
	}

	@Test
	public void singletonPerInjector() {
		Module module = new AbstractModule() {
			@Override
			protected void configure() {
				bind(IMyService.class).to(MyService.class).in(Singleton.class);
			}
		};
		assertNotSame(
			Guice.createInjector(module)
				.getInstance(MyClient.class)
				.service,
			Guice.createInjector(module)
				.getInstance(MyClient.class)
				.service
		);
	}
}
