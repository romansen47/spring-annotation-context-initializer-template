package template.test.testbeans.impl;

import org.springframework.beans.factory.annotation.Autowired;

import template.test.testbeans.TestBean;
import template.test.testbeans.TestBean2;

/**
 * Bean created using a {@link org.springframework.context.annotation.Configuration}- annotated class.
 * In this case it does not have to be annotated by {@link org.springframework.stereotype.Component}.
 * Attribute testBean2 is annotated by {@link org.springframework.stereotype.Component} and is autowired.
 * @author romansen47
 */
public class TestBeanImpl implements TestBean {

	@Autowired
	private TestBean2 testBean2;

	private static final String MESSAGE = "I was explicitly defined as a bean and testBean2 was autowired";

	/**
	 * Returns the test bean2.
	 * @return the test bean2
	 */
	@Override
	public TestBean2 getTestBean2() {
		return this.testBean2;
	}

	/**
	 * Performs the say hello operation.
	 * @return the result of the operation
	 */
	@Override
	public String sayHello() {
		return this.toString() + ": " + MESSAGE;
	}
}
