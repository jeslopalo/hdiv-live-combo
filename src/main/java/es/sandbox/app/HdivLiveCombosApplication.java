package es.sandbox.app;

import es.sandbox.ui.messages.CssClassesByLevel;
import es.sandbox.ui.messages.Level;
import es.sandbox.ui.messages.spring.config.annotation.EnableFlashMessages;
import es.sandbox.ui.messages.spring.config.annotation.FlashMessagesConfigurerAdapter;
import org.hdiv.config.Strategy;
import org.hdiv.config.annotation.ExclusionRegistry;
import org.hdiv.config.annotation.builders.SecurityConfigBuilder;
import org.hdiv.config.annotation.configuration.HdivWebSecurityConfigurerAdapter;
import org.hdiv.dataComposer.DataComposerCipher;
import org.hdiv.dataComposer.DataComposerHash;
import org.hdiv.dataComposer.IDataComposer;
import org.hdiv.util.HDIVUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SpringBootApplication
public class HdivLiveCombosApplication {

    @Bean
    public WebConfig webConfig() {
        return new WebConfig();
    }

    protected static class WebConfig extends WebMvcConfigurerAdapter {

        @Override
        public void addViewControllers(final ViewControllerRegistry registry) {

        }

        @Override
        public void addInterceptors(final InterceptorRegistry registry) {
            registry.addInterceptor(new HandlerInterceptorAdapter() {
                private static final int ALLOWED_LENGTH = 4000 * 2;

                @Override
                public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
                    LoggerFactory.getLogger(HdivLiveCombosApplication.class).info("\n=> {}", request.getRequestURL());

                    if (HDIVUtil.isDataComposer(request)) {
                        final IDataComposer dataComposer = HDIVUtil.getDataComposer();
                        if (dataComposer instanceof DataComposerHash) {
                            ((DataComposerHash) dataComposer).setAllowedLength(ALLOWED_LENGTH);
                        }
                        if (dataComposer instanceof DataComposerCipher) {
                            ((DataComposerCipher) dataComposer).setAllowedLength(ALLOWED_LENGTH);
                        }
                    }
                    return true;
                }
            });
        }
    }

    @Bean
    public ApplicationWebSecurity applicationWebSecurity() {
        return new ApplicationWebSecurity();
    }

    protected static class ApplicationWebSecurity extends HdivWebSecurityConfigurerAdapter {

        @Override
        public void configure(final SecurityConfigBuilder builder) {

            builder
                    .randomName(true)
                    .reuseExistingPageInAjaxRequest(true)
                    .strategy(Strategy.CIPHER)
                    .maxPagesPerSession(100)
                    .debugMode(false)
                    .sessionExpired()
                    .homePage("/");
        }

        @Override
        public void addExclusions(final ExclusionRegistry registry) {
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
        public void configureCssClassesByLevel(final CssClassesByLevel cssClasses) {
            cssClasses.put(Level.ERROR, "alert alert-danger");
        }
    }

    public static void main(final String[] args) {
        SpringApplication.run(HdivLiveCombosApplication.class, args);
    }
}
