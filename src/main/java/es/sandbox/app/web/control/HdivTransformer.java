package es.sandbox.app.web.control;

import org.hdiv.dataComposer.DataComposerCipher;
import org.hdiv.dataComposer.DataComposerHash;
import org.hdiv.dataComposer.IDataComposer;
import org.hdiv.util.HDIVUtil;

import java.util.Objects;

/**
 * Created by jeslopalo on 28/04/15.
 */
public class HdivTransformer implements Transformer {

    private String csrf;

    public HdivTransformer() {
    }

    private IDataComposer dataComposer() {
        return HDIVUtil.getDataComposer();
    }

    @Override
    public Option transform(final String path, final Option option) {
        return new Option(option.getLabel(), compose(path, option.getValue()));
    }

    private String compose(final String path, final Object value) {
        return dataComposer().composeFormField(path, Objects.toString(value, null), false, null);
    }

    @Override
    public String getCsrf() {

        if (this.csrf == null) {
            /* Add new state to the response */
            /* Only necessary for 'cipher' or 'hash' strategy */
            if (dataComposer() instanceof DataComposerHash || dataComposer() instanceof DataComposerCipher) {
                this.csrf = dataComposer().endRequest();
            }
        }
        return this.csrf;
    }
}
