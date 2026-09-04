package template.test.testbeans.impl;

import org.springframework.stereotype.Component;

import template.test.testbeans.TestBean2;

/**
 * Bean created using the {@link org.springframework.stereotype.Component}-annotation.
 * @author romansen47 
 */
@Component(value = "testBean2")
public class TestBean2Impl implements TestBean2 {

	private static final String MESSAGE = "I was implicitly defined as a bean and then was autowired into testBean :)";

	/**
	 * Performs the say hello operation.
	 * @return the result of the operation
	 */
	@Override
	public String sayHello() {
		return this.toString()+": "+MESSAGE;
	}

}
