package template.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import template.test.impl.SpringAnnotationContextInitializerImplForTest;
import template.test.testbeans.TestBean;
import template.test.testbeans.TestBean2;
import template.test.testconfiguration.SpringBeanConfiguration;

/**
 * @author romansen47
 */
public class TestClass {

	private static final Logger logger = LogManager.getLogger(TestClass.class);

	private static ApplicationContext applicationContext; 
	
	/**
	 * Performs the prepare operation.
	 */
	@BeforeClass
	public static void prepare() {
		applicationContext = new SpringAnnotationContextInitializerImplForTest().getApplicationContext();
	}
	
	/**
	 * Performs the test configuration operation.
	 */
	@Test
	public void testConfiguration() {
		SpringBeanConfiguration configuration = (SpringBeanConfiguration) applicationContext.getBean("springBeanConfiguration");
		var x = configuration.testBean();
		var y = configuration.testBean();
		Assert.assertEquals(x, y);
		logger.info(configuration.toString()+": "+x + " and " + y + " are same instances");
	}
	
	/**
	 * Performs the test creation of test beans operation.
	 */
	@Test
	public void testCreationOfTestBeans() {
		var testBean = applicationContext.getBean("testBean");
		Assert.assertNotNull(testBean);
		Assert.assertTrue(testBean instanceof TestBean);
		logger.info(((TestBean)testBean).sayHello());
		
		var testBean2 = ((TestBean) testBean).getTestBean2();
		Assert.assertNotNull(testBean2);
		Assert.assertTrue(testBean2 instanceof TestBean2);
		logger.info(testBean2.sayHello());
	}

}
