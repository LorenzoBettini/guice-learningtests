package guice.learningtests;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

public class GuiceBindingAnnotationTest {

	private static class MyFileWrapper {
		File file;

		@Inject
		public MyFileWrapper(@Named("PATH") String path, @Named("NAME") String name) {
			file = new File(path, name);
		}
	}

	@Test
	public void bindingAnnotations() {
		Module module = new AbstractModule() {
			@Override
			protected void configure() {
				bind(String.class)
					.annotatedWith(Names.named("PATH"))
					.toInstance("src/test/resources");
				bind(String.class)
					.annotatedWith(Names.named("NAME"))
					.toInstance("afile.txt");
			}
		};
		Injector injector = Guice.createInjector(module);
		MyFileWrapper fileWrapper = injector.getInstance(MyFileWrapper.class);
		assertTrue(fileWrapper.file.exists());
	}
}
