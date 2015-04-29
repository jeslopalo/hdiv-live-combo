package es.sandbox.app.web.control;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;

/**
 * Created by jeslopalo on 24/04/15.
 */
public class Option implements Comparable<Option> {
    private String label;
    private Object value;

    public Option(String label, Object value) {
        this.label = label;
        this.value = value;
    }

    public Option(String label) {
        this.label = label;
        this.value = null;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    boolean isEmpty() {
        return StringUtils.isEmpty(Objects.toString(this.value, ""));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
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
