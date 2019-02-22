package guice.learningtests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class GuiceCyclicDependenciesLearningTest {

	private static interface IMyView {

	}

	private static class MyView implements IMyView {
		IMyController controller;

		public void setController(IMyController controller) {
			this.controller = controller;
		}
	}

	private static interface IMyRepository {

	}

	private static class MyRepository implements IMyRepository {

	}

	private static interface IMyController {

	}

	private static class MyController implements IMyController {
		IMyView view;
		IMyRepository repository;

		@Inject
		public MyController(
			@Assisted IMyView view, // from the instance's creator
			IMyRepository repository // from the Injector
		) {
			this.view = view;
			this.repository = repository;
		}
	}

	private static interface MyControllerFactory {
		IMyController create(IMyView view);
	}

	private static class MyViewProvider implements Provider<MyView> {
		@Inject
		private MyControllerFactory controllerFactory;

		@Override
		public MyView get() {
			// manually solve the cycle
			MyView view = new MyView();
			view.setController(controllerFactory.create(view));
			return view;
		}
	}

	@Test
	public void cyclicDependencies() {
		Module module = new AbstractModule() {
			@Override
			protected void configure() {
				bind(IMyRepository.class).to(MyRepository.class);
				bind(MyView.class).toProvider(MyViewProvider.class);
				install(new FactoryModuleBuilder()
					.implement(IMyController.class, MyController.class)
					.build(MyControllerFactory.class));
			}
		};
		Injector injector = Guice.createInjector(module);
		MyView view = injector.getInstance(MyView.class);
		assertSame(view, ((MyController) view.controller).view);
		assertNotNull(((MyController) view.controller).repository);
	}

	@Test
	public void providesMethod() {
		Module module = new AbstractModule() {
			@Override
			protected void configure() {
				bind(IMyRepository.class).to(MyRepository.class);
				install(new FactoryModuleBuilder()
					.implement(IMyController.class, MyController.class)
					.build(MyControllerFactory.class));
				// don't bind the provider here
			}

			@Provides // the parameter will be injected
			MyView view(MyControllerFactory controllerFactory) {
				MyView view = new MyView();
				view.setController(controllerFactory.create(view));
				return view;
			}
		};
		Injector injector = Guice.createInjector(module);
		MyView view = injector.getInstance(MyView.class);
		assertSame(view, ((MyController) view.controller).view);
		assertNotNull(((MyController) view.controller).repository);
	}
}
