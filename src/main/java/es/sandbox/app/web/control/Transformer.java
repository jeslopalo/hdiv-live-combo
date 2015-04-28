package es.sandbox.app.web.control;

/**
 * Created by jeslopalo on 28/04/15.
 */
public interface Transformer {

    Option transform(String path, Option option);

    String getCsrf();

    public static class NopTransformer implements Transformer {

        @Override
        public Option transform(String path, Option option) {
            return option;
        }

        @Override
        public String getCsrf() {
            return null;
        }
    }
}
