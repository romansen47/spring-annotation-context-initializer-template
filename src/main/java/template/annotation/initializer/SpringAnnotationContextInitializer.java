package template.annotation.initializer;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * Base template for the initializer class
 * @author romansen47
 *
 */
public abstract class SpringAnnotationContextInitializer implements ApplicationContextAware {

	private static final Logger logger = LogManager.getLogger(SpringAnnotationContextInitializer.class);

	private ApplicationContext applicationContext;

	/**
	 * Creates a new SpringAnnotationContextInitializer instance.
	 */
	public SpringAnnotationContextInitializer() {
		this.applicationContext = new AnnotationConfigApplicationContext();
		this.updateLoggers();
		this.getBeans();
	}

	/**
	 * Updates the loggers.
	 */
	private void updateLoggers() {
		final var ctx = (LoggerContext) LogManager.getContext(false);
		final var config = ctx.getConfiguration();
		final var loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
		loggerConfig.setLevel(Level.INFO);
		ctx.updateLoggers();
	}

	/**
	 * Returns the beans.
	 */
	private void getBeans() {
		SpringAnnotationContextInitializer.logger.info("applicationContext {} scanning in definitions..*",
				this.applicationContext.toString().split(",")[0]);
		((AnnotationConfigApplicationContext) this.applicationContext).scan(this.getBasePackages());
		SpringAnnotationContextInitializer.logger.info("applicationContext {} refreshing",
				this.applicationContext.toString().split(",")[0]);
		((AbstractApplicationContext) this.applicationContext).refresh();
		SpringAnnotationContextInitializer.logger.info("Beans we are aware of:");
		for (final String beanName : this.applicationContext.getBeanNamesForType(Object.class)) {
			SpringAnnotationContextInitializer.logger.info("bean " + beanName);
		}
	}

	/**
	 * Returns the application context.
	 * @return the application context
	 */
	public ApplicationContext getApplicationContext() {
		return this.applicationContext;
	}

	/**
	 * Sets the application context.
	 * @param applicationContext the application context
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * Returns the base packages.
	 * @return the base packages
	 */
	public abstract String getBasePackages();

}
