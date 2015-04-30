package es.sandbox.app.web.control;

import es.sandbox.app.web.control.Transformer.NopTransformer;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.SetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by jeslopalo on 24/04/15.
 */
public class Selector {
    private static final Logger LOGGER = LoggerFactory.getLogger(Selector.class);

    private String path;

    private final String unselectedOptionLabel;

    private final OnSelectPopulator onSelectPopulator;

    private SortedSet<Option> options;
    private String csrf;
    private Transformer transformer;

    private transient SortedSet<Option> transformedOptions;
    private transient Map<String, String> transformedUrls;

    public Selector(final SelectorBuilder selectorBuilder, final SortedSet<Option> options) {
        this.path = selectorBuilder.path;

        this.unselectedOptionLabel = selectorBuilder.unselectedOptionLabel;

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

    public String getUnselectedOptionLabel() {
        return this.unselectedOptionLabel;
    }

    public String getPopulatePath() {
        return onSelectPopulator.getPopulatePath();
    }

    public SortedSet<Option> getOptions() {
        if (this.transformedOptions == null) {
            LOGGER.debug("Getting options for path [{}]...", this.path);
            this.transformedOptions = new TreeSet<>();
            for (final Option option : this.options) {
                option.setValue(getTransformer(), this.path);
                this.transformedOptions.add(option);
            }
        }
        return SetUtils.unmodifiableSortedSet(this.transformedOptions);
    }

    public Map<String, String> getUrls() {
        if (this.transformedUrls == null) {
            LOGGER.debug("Getting urls for path [{}]", this.path);
            this.transformedUrls = this.onSelectPopulator.getUrls(this.transformer, this.options);
        }
        return MapUtils.unmodifiableMap(this.transformedUrls);
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

        private String unselectedOptionLabel;

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

        public SelectorBuilder withUnselectedOptionLabel(final String unselectedOptionLabel) {
            this.unselectedOptionLabel = unselectedOptionLabel;
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
