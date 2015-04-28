package es.sandbox.app.web.control;

import es.sandbox.app.web.control.Transformer.NopTransformer;

import java.util.*;

/**
 * Created by jeslopalo on 24/04/15.
 */
public class Selector {

    private String path;
    private final String urlPattern;
    private SortedSet<Option> options;
    private String csrf;
    private Transformer transformer;

    public Selector(final String path) {
        this(path, null);
    }

    public Selector(final String path, final String urlPattern) {
        this.path = path;
        this.urlPattern = urlPattern;
        this.options = new TreeSet<>();
        this.transformer = new NopTransformer();
    }

    public void setTransformer(final Transformer transformer) {
        if (transformer != null) {
            this.transformer = transformer;
        }
    }

    private Transformer getTransformer() {
        return this.transformer;
    }

    public String getPath() {
        return this.path;
    }

    public void add(final Option option) {
        this.options.add(option);
    }

    public SortedSet<Option> getOptions() {
        final SortedSet<Option> sortedOptions = new TreeSet<>();
        for (Option option : this.options) {
            sortedOptions.add(getTransformer().transform(this.path, option));
        }
        return sortedOptions;
    }

    public Map<String, String> getUrls() {
        final Map<String, String> urls = new HashMap<>(options.size());

        for (final Option option : options) {
            if (!option.isEmpty()) {
                urls.put(Objects.toString(option.getValue()), url(option));
            }
        }

        return urls;
    }

    private String url(final Option option) {
        if (option.isEmpty()) {
            return "";
        }
        return String.format(this.urlPattern, option.getValue());
    }

    public String getCsrf() {
        return getTransformer().getCsrf();
    }
}
