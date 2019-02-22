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
import com.google.inject.util.Modules;

public class GuiceModulesOverrideLearningTest {

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
	public void modulesOverride() {
		Module defaultModule = new AbstractModule() {
			@Override
			protected void configure() {
				bind(IMyService.class).to(MyService.class);
			}
		};
		Injector injector = Guice.createInjector(defaultModule);
		MyClient client1 = injector.getInstance(MyClient.class);
		MyClient client2 = injector.getInstance(MyClient.class);
		// not singleton
		assertNotSame(client1.service, client2.service);
		Module customModule = new AbstractModule() {
			@Override
			protected void configure() {
				bind(MyService.class).in(Singleton.class);
			}
		};
		injector = Guice.createInjector(
			Modules.override(defaultModule).with(customModule)
		);
		client1 = injector.getInstance(MyClient.class);
		client2 = injector.getInstance(MyClient.class);
		assertNotNull(client1.service);
		// now it is singleton
		assertSame(client1.service, client2.service);
	}
}
