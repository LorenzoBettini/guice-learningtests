package guice.learningtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;

public class GuiceLearningTest {

	private static interface IMyService {

	}

	private static class MyService implements IMyService {

	}

	private static class MyClient {
		MyService service;

		@Inject
		public MyClient(MyService service) {
			this.service = service;
		}
	}

	private static class MyGenericClient {
		IMyService service;

		@Inject
		public MyGenericClient(IMyService service) {
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

	@Test
	public void injectAbstractType() {
		Module module = new AbstractModule() {
			@Override
			protected void configure() {
				bind(IMyService.class).to(MyService.class);
			}
		};
		Injector injector = Guice.createInjector(module);
		MyGenericClient client = injector.getInstance(MyGenericClient.class);
		assertNotNull(client.service);
		assertEquals(MyService.class, client.service.getClass());
	}

	@Test
	public void bindToInstance() {
		Module module = new AbstractModule() {
			@Override
			protected void configure() {
				bind(IMyService.class).toInstance(new MyService());
			}
		};
		Injector injector = Guice.createInjector(module);
		MyGenericClient client1 = injector.getInstance(MyGenericClient.class);
		MyGenericClient client2 = injector.getInstance(MyGenericClient.class);
		assertNotNull(client1.service);
		assertSame(client1.service, client2.service);
	}

	@Test
	public void bindToInstanceReusedByInjectors() {
		final MyService instance = new MyService();
		Module module = new AbstractModule() {
			@Override
			protected void configure() {
				bind(IMyService.class).toInstance(instance);
			}
		};
		assertSame(
			Guice.createInjector(module)
				.getInstance(MyGenericClient.class)
				.service,
			Guice.createInjector(module)
				.getInstance(MyGenericClient.class)
				.service
		);
	}

	@Test
	public void bindToInstanceNotReusedByInjectors() {
		Module module = new AbstractModule() {
			@Override
			protected void configure() {
				bind(IMyService.class).toInstance(new MyService());
			}
		};
		assertNotSame(
			Guice.createInjector(module)
				.getInstance(MyGenericClient.class)
				.service,
			Guice.createInjector(module)
				.getInstance(MyGenericClient.class)
				.service
		);
	}
}
