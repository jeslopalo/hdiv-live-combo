package es.sandbox.app.web;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by jeslopalo on 24/04/15.
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
        return firstValue;
    }

    public void setFirstValue(String firstValue) {
        this.firstValue = firstValue;
    }

    public String getSecondValue() {
        return secondValue;
    }

    public void setSecondValue(String secondValue) {
        this.secondValue = secondValue;
    }

    public String getThirdValue() {
        return thirdValue;
    }

    public void setThirdValue(String thirdValue) {
        this.thirdValue = thirdValue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("firstValue", firstValue)
                .append("secondValue", secondValue)
                .append("thirdValue", thirdValue)
                .toString();
    }
}
