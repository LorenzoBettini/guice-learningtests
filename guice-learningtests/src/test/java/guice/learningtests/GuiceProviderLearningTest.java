package guice.learningtests;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provider;

public class GuiceProviderLearningTest {

	private static interface IMyService {

	}

	private static class MyService implements IMyService {

	}

	private static class MyClientWithInjectedProvider {
		@Inject
		Provider<IMyService> serviceProvider;

		IMyService getService() {
			return serviceProvider.get();
		}
	}

	@Test
	public void injectProviderExample() {
		Module module = new AbstractModule() {
			@Override
			protected void configure() {
				bind(IMyService.class).to(MyService.class);
			}
		};
		Injector injector = Guice.createInjector(module);
		MyClientWithInjectedProvider client = injector.getInstance(MyClientWithInjectedProvider.class);
		assertNotNull(client.getService());
	}
}
