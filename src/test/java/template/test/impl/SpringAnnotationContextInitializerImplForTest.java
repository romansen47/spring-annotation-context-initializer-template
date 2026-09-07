package template.test.impl;

import template.annotation.initializer.SpringAnnotationContextInitializer;

/**
 * Test implementation that scans the complete {@code template.test} package.
 */
public class SpringAnnotationContextInitializerImplForTest extends SpringAnnotationContextInitializer {

    public static final String BASE_PACKAGES = "template.test";

    /**
     * Returns the package used by the integration tests.
     *
     * @return {@value #BASE_PACKAGES}
     */
    @Override
    protected String getBasePackages() {
        return BASE_PACKAGES;
    }
}
