package es.sandbox.app.web.control;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by jeslopalo on 24/04/15.
 */
public class Option implements Comparable<Option> {
    private String label;
    private Object value;

    private String dataValue;
    private String url;

    public Option(String label, Object value) {
        this(label, value, null);
    }

    public Option(String label, Object value, String url) {
        this.label = label;
        this.dataValue = Objects.toString(value, null);

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
        return url;
    }

    void setUrl(final Transformer transformer, final String urlPattern) {
        if (this.url == null) {
            this.url = transformer.transform(url(urlPattern));
        }
    }

    private String url(final String urlPattern) {
        if (isEmpty() || isBlank(urlPattern)) {
            return null;
        }
        return format(urlPattern, this.value);
    }


    boolean isEmpty() {
        return StringUtils.isEmpty(Objects.toString(this.value, ""));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append("label", label)
                .append("value", value)
                .toString();
    }

    @Override
    public int compareTo(Option o) {
        if (this.equals(o)) {
            return 0;
        }
        if (this.value == null) {
            return -1;
        }
        if (o.value == null) {
            return 1;
        }
        if (!equalsIgnoreCase(this.label, o.label)) {
            return this.label.compareTo(o.label);
        }
        return this.value.toString().compareTo(o.value.toString());
    }
}
