package template.test;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import template.annotation.initializer.SpringAnnotationContextInitializer;
import template.test.impl.SpringAnnotationContextInitializerImplForTest;
import template.test.testbeans.TestBean;
import template.test.testbeans.TestBean2;
import template.test.testconfiguration.SpringBeanConfiguration;

/**
 * Integration tests for {@link SpringAnnotationContextInitializer}.
 */
public class TestClass {

    private static SpringAnnotationContextInitializerImplForTest initializer;
    private static ApplicationContext applicationContext;

    /**
     * Creates the test initializer once for all integration tests.
     */
    @BeforeClass
    public static void prepare() {
        initializer = new SpringAnnotationContextInitializerImplForTest();
        applicationContext = initializer.getApplicationContext();
    }

    /**
     * Closes the internally created application context after the tests.
     */
    @AfterClass
    public static void cleanup() {
        if (applicationContext instanceof AnnotationConfigApplicationContext context) {
            context.close();
        }
    }

    /**
     * Verifies that construction creates and refreshes an annotation-based
     * application context.
     */
    @Test
    public void createsRefreshedApplicationContext() {
        Assert.assertNotNull(applicationContext);
        Assert.assertTrue(applicationContext instanceof AnnotationConfigApplicationContext);
        Assert.assertTrue(((AnnotationConfigApplicationContext) applicationContext).isActive());
    }

    /**
     * Verifies that configuration classes discovered through component scanning
     * participate in normal Spring singleton semantics.
     */
    @Test
    public void discoversConfigurationAndKeepsBeanSingleton() {
        SpringBeanConfiguration configuration = applicationContext.getBean(SpringBeanConfiguration.class);
        TestBean first = configuration.testBean();
        TestBean second = configuration.testBean();

        Assert.assertSame(first, second);
    }

    /**
     * Verifies discovery of an explicitly configured bean and autowiring of a
     * component-scanned dependency.
     */
    @Test
    public void createsAndAutowiresTestBeans() {
        TestBean testBean = applicationContext.getBean(TestBean.class);
        Assert.assertNotNull(testBean);

        TestBean2 testBean2 = testBean.getTestBean2();
        Assert.assertNotNull(testBean2);
        Assert.assertSame(applicationContext.getBean(TestBean2.class), testBean2);
    }

    /**
     * Verifies the {@code ApplicationContextAware} callback contract.
     */
    @Test
    public void replacesExposedApplicationContext() {
        GenericApplicationContext replacement = new GenericApplicationContext();
        try {
            initializer.setApplicationContext(replacement);
            Assert.assertSame(replacement, initializer.getApplicationContext());
        } finally {
            initializer.setApplicationContext(applicationContext);
            replacement.close();
        }
    }

    /**
     * Verifies that an invalid scan configuration fails immediately with a
     * descriptive exception.
     */
    @Test
    public void rejectsBlankBasePackage() {
        try {
            new SpringAnnotationContextInitializer() {
                @Override
                protected String getBasePackages() {
                    return " ";
                }
            };
            Assert.fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            Assert.assertTrue(e.getMessage().contains("must not be blank"));
        }
    }
}
