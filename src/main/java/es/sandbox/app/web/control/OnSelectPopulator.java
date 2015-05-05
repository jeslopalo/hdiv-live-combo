package es.sandbox.app.web.control;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

final class OnSelectPopulator {

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
                option.setUrl(transformer, this.urlPattern);

                urls.put(option.getDataValue(), option.getUrl());
            }
        }
        return urls;
    }
}