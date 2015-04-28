package es.sandbox.app;

import es.sandbox.ui.messages.spring.config.annotation.EnableFlashMessages;
import org.hdiv.config.Strategy;
import org.hdiv.config.annotation.ExclusionRegistry;
import org.hdiv.config.annotation.builders.SecurityConfigBuilder;
import org.hdiv.config.annotation.configuration.HdivWebSecurityConfigurerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableFlashMessages
public class HdivLiveCombosApplication {

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
                    .strategy(Strategy.CIPHER)
                    .sessionExpired()
                    .homePage("/");
        }

        @Override
        public void addExclusions(ExclusionRegistry registry) {
            registry.addUrlExclusions("/");
        }
    }

    /*@Bean
    public FlashMessagesConfig flashMessagesConfig() {
        return new FlashMessagesConfig();
    }*/

//    @EnableFlashMessages
//    protected static class FlashMessagesConfig extends FlashMessagesConfigurerAdapter {
//
//        private static final Logger LOGGER= LoggerFactory.getLogger(FlashMessagesConfig.class);
//
//        public FlashMessagesConfig() {
//            LOGGER.info("Loading Flash Messages configuration...");
//        }
//
//        /**
//         * Sets the styles of flash-messages to be compatible
//         * with twitter bootstrap alerts
//         */
//        @Override
//        public void configureCssClassesByLevel(CssClassesByLevel cssClasses) {
//            cssClasses.put(Level.ERROR, "alert alert-danger");
//        }
//    }

    public static void main(String[] args) {
        SpringApplication.run(HdivLiveCombosApplication.class, args);
    }
}
