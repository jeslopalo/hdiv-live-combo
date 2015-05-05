package es.sandbox.app.web;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author jeslopalo
 * @since 24/04/15.
 */
public class LiveCombosForm {

    @NotNull
    @Size(min = 1)
    private String firstValue;

    @NotNull
    @Size(min = 1)
    private String secondValue;

    @NotNull
    @Size(min = 1)
    private String thirdValue;

    public String getFirstValue() {
        return this.firstValue;
    }

    public void setFirstValue(final String firstValue) {
        this.firstValue = firstValue;
    }

    public String getSecondValue() {
        return this.secondValue;
    }

    public void setSecondValue(final String secondValue) {
        this.secondValue = secondValue;
    }

    public String getThirdValue() {
        return this.thirdValue;
    }

    public void setThirdValue(final String thirdValue) {
        this.thirdValue = thirdValue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("firstValue", this.firstValue)
                .append("secondValue", this.secondValue)
                .append("thirdValue", this.thirdValue)
                .toString();
    }
}
