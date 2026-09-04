package template.test.testconfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import template.test.testbeans.TestBean;
import template.test.testbeans.impl.TestBeanImpl;

/**
 * Configuration class for bean declaration. Same effect if this class is deleted and testBeans are annotated with {@link org.springframework.stereotype.Component}
 * @author romansen47
 */
@Configuration
public class SpringBeanConfiguration {

	/**
	 * Performs the test bean operation.
	 * @return the result of the operation
	 */
	@Bean
	public TestBean testBean() {
		return new TestBeanImpl();
	}

}
