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

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author jeslopalo
 * @since 24/04/15.
 */
public class Selector {
    private static final Logger LOGGER = LoggerFactory.getLogger(Selector.class);

    private final String path;
    private final OnSelectPopulator onSelectPopulator;

    private final SortedSet<Option> options;
    private final Transformer transformer;

    private transient SortedSet<Option> transformedOptions;
    private transient Map<String, String> transformedUrls;

    public Selector(final SelectorBuilder selectorBuilder, final SortedSet<Option> options) {
        this.path = selectorBuilder.path;

        this.onSelectPopulator = selectorBuilder.onSelectPopulator;
        this.transformer = selectorBuilder.transformer;

        this.options = addUnselectedOption(options, selectorBuilder.unselectedOptionLabel);
    }

    private SortedSet<Option> addUnselectedOption(final SortedSet<Option> options, final String unselectedOptionLabel) {
        final SortedSet<Option> internalOptions = new TreeSet<>();

        if (isNotBlank(unselectedOptionLabel)) {
            final Option unselectedOption = new Option(unselectedOptionLabel, null);
            internalOptions.add(unselectedOption);
        }
        internalOptions.addAll(options);
        return internalOptions;
    }

    private Transformer getTransformer() {
        return this.transformer;
    }

    public String getPath() {
        return this.path;
    }

    public String getPopulatePath() {
        return this.onSelectPopulator.getPopulatePath();
    }

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
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


    public static SelectorBuilder builderForPath(final String path) {
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
