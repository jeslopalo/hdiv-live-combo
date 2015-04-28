package es.sandbox.app.web.control;

import org.apache.commons.collections.SetUtils;

import java.util.*;

/**
 * Created by jeslopalo on 24/04/15.
 */
public class Selector {

    private String path;
    private final String urlPattern;
    private SortedSet<Option> options;
    private String hdivFormState;

    public Selector(final String path) {
        this(path, null);
    }

    public Selector(final String path, final String urlPattern) {
        this.path = path;
        this.urlPattern = urlPattern;
        this.options = new TreeSet<>();
    }

    public String getPath() {
        return path;
    }

    public void add(final Option option) {
        this.options.add(option);
    }

    public SortedSet<Option> getOptions() {
        return SetUtils.unmodifiableSortedSet(this.options);
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

    public String getHdivFormState() {
        return hdivFormState;
    }
}
