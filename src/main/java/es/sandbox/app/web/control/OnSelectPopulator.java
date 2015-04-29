package es.sandbox.app.web.control;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;

class OnSelectPopulator {

    private final String populatePath;
    private final String urlPattern;

    OnSelectPopulator() {
        this.populatePath = null;
        this.urlPattern = null;
    }

    OnSelectPopulator(final String populatePath, final String urlPattern) {
        this.populatePath = populatePath;
        this.urlPattern = urlPattern;
    }

    public String getPopulatePath() {
        return this.populatePath;
    }

    public Map<String, String> getUrls(final Transformer transformer, final SortedSet<Option> options) {
        final Map<String, String> urls = new HashMap<>(options.size());

        if (StringUtils.isNotBlank(this.urlPattern)) {
            for (final Option option : options) {
                if (!option.isEmpty()) {
                    urls.put(Objects.toString(option.getValue()), transformer.transform(url(option)));
                }
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
}