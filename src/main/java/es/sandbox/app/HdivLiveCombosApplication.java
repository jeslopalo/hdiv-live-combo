package es.sandbox.app;

import es.sandbox.app.web.control.HdivTransformer;
import es.sandbox.ui.messages.CssClassesByLevel;
import es.sandbox.ui.messages.Level;
import es.sandbox.ui.messages.spring.config.annotation.EnableFlashMessages;
import es.sandbox.ui.messages.spring.config.annotation.FlashMessagesConfigurerAdapter;
import org.hdiv.config.Strategy;
import org.hdiv.config.annotation.ExclusionRegistry;
import org.hdiv.config.annotation.builders.SecurityConfigBuilder;
import org.hdiv.config.annotation.configuration.HdivWebSecurityConfigurerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.ServletContext;

@SpringBootApplication
public class HdivLiveCombosApplication implements ServletContextAware {

    private ServletContext servletContext;

    @Bean
    public HdivTransformer hdivTransformer() {
        return new HdivTransformer(this.servletContext);
    }

    @Override
    public void setServletContext(final ServletContext servletContext) {
        this.servletContext = servletContext;
    }


    @Bean
    public WebConfig webConfig() {
        return new WebConfig();
    }

    protected static class WebConfig extends WebMvcConfigurerAdapter {

        @Override
        public void addViewControllers(ViewControllerRegistry registry) {

        }
    }

    @Bean
    public ApplicationWebSecurity applicationWebSecurity() {
        return new ApplicationWebSecurity();
    }

    protected static class ApplicationWebSecurity extends HdivWebSecurityConfigurerAdapter {

        @Override
        public void configure(SecurityConfigBuilder builder) {

            builder
                    .randomName(true)
                    .reuseExistingPageInAjaxRequest(true)
                    .strategy(Strategy.MEMORY)
                    .maxPagesPerSession(100)
                    .debugMode(false)
                    .sessionExpired()
                    .homePage("/");
        }

        @Override
        public void addExclusions(ExclusionRegistry registry) {
            registry.addUrlExclusions("/");
        }
    }

    @EnableFlashMessages
    protected static class FlashMessagesConfig extends FlashMessagesConfigurerAdapter {

        private static final Logger LOGGER = LoggerFactory.getLogger(FlashMessagesConfig.class);

        public FlashMessagesConfig() {
            LOGGER.info("Loading Flash Messages configuration...");
        }

        /**
         * Sets the styles of flash-messages to be compatible
         * with twitter bootstrap alerts
         */
        @Override
        public void configureCssClassesByLevel(CssClassesByLevel cssClasses) {
            cssClasses.put(Level.ERROR, "alert alert-danger");
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(HdivLiveCombosApplication.class, args);
    }
}
