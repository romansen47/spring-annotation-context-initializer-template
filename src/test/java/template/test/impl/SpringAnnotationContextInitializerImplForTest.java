package template.test.impl;

import org.springframework.context.annotation.ComponentScan;

import template.annotation.initializer.SpringAnnotationContextInitializer;

/**
 * Concrete implementation of the annotation context initializer.
 * 
 * Root package is template, only sub-packages are scanned for bean definition pick up.
 * @author romansen47
 */
@ComponentScan(basePackages = SpringAnnotationContextInitializerImplForTest.BASE_PACKAGES)
public class SpringAnnotationContextInitializerImplForTest extends SpringAnnotationContextInitializer {

	public static final String BASE_PACKAGES = "template..*";

	/**
	 * Returns the base packages.
	 * @return the base packages
	 */
	@Override
	public String getBasePackages() {
		return SpringAnnotationContextInitializerImplForTest.BASE_PACKAGES;
	}

}
