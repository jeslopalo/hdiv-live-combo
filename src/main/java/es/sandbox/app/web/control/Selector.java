package es.sandbox.app.web.control;

import es.sandbox.app.web.control.Transformer.NopTransformer;

import java.util.Arrays;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by jeslopalo on 24/04/15.
 */
public class Selector {

    private String path;

    private final OnSelectPopulator onSelectPopulator;

    private SortedSet<Option> options;
    private String csrf;
    private Transformer transformer;

    public Selector(final SelectorBuilder selectorBuilder, final SortedSet<Option> options) {
        this.path = selectorBuilder.path;

        this.onSelectPopulator = selectorBuilder.onSelectPopulator;
        this.transformer = selectorBuilder.transformer;

        this.options = options;
    }

    private Transformer getTransformer() {
        return this.transformer;
    }

    public String getPath() {
        return this.path;
    }

    public String getPopulatePath() {
        return onSelectPopulator.getPopulatePath();
    }

    public SortedSet<Option> getOptions() {
        final SortedSet<Option> sortedOptions = new TreeSet<>();
        for (final Option option : this.options) {
            sortedOptions.add(getTransformer().transform(this.path, option));
        }
        return sortedOptions;
    }

    public Map<String, String> getUrls() {
        return this.onSelectPopulator.getUrls(this.transformer, this.options);
    }

    public String getCsrf() {
        return getTransformer().getCsrf();
    }


    public static final SelectorBuilder builderForPath(final String path) {
        return new SelectorBuilder(path);
    }

    public static final class SelectorBuilder {
        private final String path;

        private SortedSet<Option> options;
        private Transformer transformer;

        private OnSelectPopulator onSelectPopulator;

        private SelectorBuilder(final String path) {
            this.path = path;
            this.options = new TreeSet<>();
            this.transformer = new NopTransformer();
            this.onSelectPopulator = new OnSelectPopulator();
        }

        public SelectorBuilder onSelect(final String populatePath, final String urlPattern) {
            this.onSelectPopulator = new OnSelectPopulator(populatePath, urlPattern);
            return this;
        }

        public SelectorBuilder withTransformer(final Transformer transformer) {
            if (transformer != null) {
                this.transformer = transformer;
            }
            return this;
        }

        public SelectorBuilder addOption(final Option option) {
            if (option != null) {
                this.options.add(option);
            }
            return this;
        }

        public SelectorBuilder withOptions(final Option... options) {
            if (options == null) {
                this.options.clear();
                return this;
            }
            this.options = new TreeSet<>(Arrays.asList(options));
            return this;
        }

        public final Selector build() {
            return new Selector(this, this.options);
        }
    }
}
