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
 * Base class for bootstrapping a Spring annotation-based application context.
 *
 * <p>Creating a concrete subclass immediately creates an
 * {@link AnnotationConfigApplicationContext}, scans the package returned by
 * {@link #getBasePackages()}, refreshes the context, and therefore makes all
 * discovered Spring components and configuration classes available through
 * {@link #getApplicationContext()}.</p>
 *
 * <p>The implementation is intended for small applications or integration
 * layers that want to obtain a Spring context without introducing a larger
 * framework bootstrap. Subclasses only need to provide the package that should
 * be scanned.</p>
 *
 * <p><strong>Subclassing note:</strong> {@link #getBasePackages()} is invoked
 * from this class's constructor. Implementations should therefore return a
 * constant or another value that is already safe to access during superclass
 * construction and must not depend on subclass fields initialized afterwards.</p>
 *
 * @author romansen47
 */
public abstract class SpringAnnotationContextInitializer implements ApplicationContextAware {

    private static final Logger logger = LogManager.getLogger(SpringAnnotationContextInitializer.class);

    private ApplicationContext applicationContext;

    /**
     * Creates, scans, and refreshes a new annotation-based Spring application
     * context.
     *
     * <p>Instantiation is eager: when this constructor returns, the context has
     * already been refreshed and discovered beans can be retrieved immediately.</p>
     */
    protected SpringAnnotationContextInitializer() {
        this.applicationContext = new AnnotationConfigApplicationContext();
        updateLoggers();
        initializeBeans();
    }

    /**
     * Sets the root Log4j logger level to {@link Level#INFO} for the current
     * logger context.
     */
    private void updateLoggers() {
        final var ctx = (LoggerContext) LogManager.getContext(false);
        final var config = ctx.getConfiguration();
        final var loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
        loggerConfig.setLevel(Level.INFO);
        ctx.updateLoggers();
    }

    /**
     * Scans the configured base package, refreshes the context, and logs the
     * bean names visible through the resulting application context.
     */
    private void initializeBeans() {
        String basePackages = getBasePackages();
        if (basePackages == null || basePackages.isBlank()) {
            throw new IllegalStateException("Base package for Spring component scanning must not be blank");
        }

        logger.info("applicationContext {} scanning in {}",
                this.applicationContext.toString().split(",")[0],
                basePackages);
        ((AnnotationConfigApplicationContext) this.applicationContext).scan(basePackages);
        logger.info("applicationContext {} refreshing",
                this.applicationContext.toString().split(",")[0]);
        ((AbstractApplicationContext) this.applicationContext).refresh();
        logger.info("Beans we are aware of:");
        for (final String beanName : this.applicationContext.getBeanNamesForType(Object.class)) {
            logger.info("bean {}", beanName);
        }
    }

    /**
     * Returns the application context managed by this initializer.
     *
     * <p>Immediately after construction this is the internally created and
     * refreshed {@link AnnotationConfigApplicationContext}. When Spring itself
     * invokes {@link #setApplicationContext(ApplicationContext)}, the supplied
     * context becomes the value returned here.</p>
     *
     * @return the current application context
     */
    public ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    /**
     * Replaces the context reference exposed by this initializer.
     *
     * <p>This method implements Spring's {@link ApplicationContextAware}
     * contract. It does not scan or refresh the supplied context.</p>
     *
     * @param applicationContext application context supplied by Spring
     * @throws BeansException when Spring cannot apply the aware callback
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * Returns the base package used for component scanning.
     *
     * <p>Return a normal Java package name such as {@code com.example.app}.
     * Spring scans that package and its subpackages recursively.</p>
     *
     * @return non-blank base package name
     */
    protected abstract String getBasePackages();
}
