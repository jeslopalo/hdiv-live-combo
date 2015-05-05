package es.sandbox.app.web.control;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author jeslopalo
 * @since 24/04/15.
 */
public class Option implements Comparable<Option> {
    private final String label;
    private Object value;

    private final String dataValue;
    private String url;

    public Option(final String label, final Object value) {
        this(label, value, null);
    }

    public Option(final String label, final Object value, final String url) {
        this.label = label;
        this.dataValue = Objects.toString(value, "");

        this.url = url;
    }

    public String getLabel() {
        return this.label;
    }

    public Object getValue() {
        return this.value;
    }

    void setValue(final Transformer transformer, final String path) {
        if (this.value == null) {
            this.value = transformer.transform(path, this.dataValue);
        }
    }

    public String getDataValue() {
        return this.dataValue;
    }

    public String getUrl() {
        return this.url;
    }

    void setUrl(final Transformer transformer, final String urlPattern) {
        if (this.url == null) {
            this.url = transformer.transform(url(urlPattern));
        }
    }

    private String url(final String urlPattern) {
        if (isBlank(urlPattern)) {
            return null;
        }
        return format(urlPattern, Objects.toString(this.dataValue, ""));
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append("label", this.label)
                .append("value", this.value)
                .toString();
    }

    @Override
    public int compareTo(final Option o) {
        if (this.equals(o)) {
            return 0;
        }
        if (isBlank(Objects.toString(this.dataValue, null))) {
            return -1;
        }
        if (isBlank(Objects.toString(o.dataValue, null))) {
            return 1;
        }
        if (!equalsIgnoreCase(this.label, o.label)) {
            return this.label.compareTo(o.label);
        }
        return this.dataValue.toString().compareTo(o.dataValue.toString());
    }
}
