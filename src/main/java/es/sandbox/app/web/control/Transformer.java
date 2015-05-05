package es.sandbox.app.web.control;

import java.util.Objects;

/**
 * @author jeslopalo
 * @since 28/04/15.
 */
public interface Transformer {

    String transform(String path, Object value);

    String transform(String url);

    String getCsrf();


    class NopTransformer implements Transformer {

        @Override
        public String transform(final String path, final Object value) {
            return Objects.toString(value, null);
        }

        @Override
        public String transform(final String url) {
            return url;
        }

        @Override
        public String getCsrf() {
            return null;
        }
    }
}
