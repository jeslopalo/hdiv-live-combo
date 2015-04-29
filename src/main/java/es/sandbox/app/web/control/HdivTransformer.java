package es.sandbox.app.web.control;

import org.apache.commons.lang3.StringUtils;
import org.hdiv.dataComposer.DataComposerCipher;
import org.hdiv.dataComposer.DataComposerHash;
import org.hdiv.dataComposer.IDataComposer;
import org.hdiv.urlProcessor.FormUrlProcessor;
import org.hdiv.urlProcessor.LinkUrlProcessor;
import org.hdiv.util.Constants;
import org.hdiv.util.HDIVUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * Created by jeslopalo on 28/04/15.
 */
public class HdivTransformer implements Transformer {

    private static final Logger LOGGER = LoggerFactory.getLogger(HdivTransformer.class);

    private String csrf;
    private ServletContext servletContext;

    public HdivTransformer(final ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    private IDataComposer dataComposer() {
        return HDIVUtil.getDataComposer();
    }

    @Override
    public Option transform(final String path, final Option option) {
        return new Option(option.getLabel(), compose(path, option.getValue()));
    }

    private String compose(final String path, final Object value) {
        LOGGER.debug("Composing form [{}] field value: [{}] (request started: {})", path, value, dataComposer().isRequestStarted());
        return dataComposer().composeFormField(path, Objects.toString(value, null), false, null);
    }

    @Override
    public String transform(final String url) {
        if (StringUtils.isBlank(url)) {
            return url;
        }
        final HttpServletRequest httpServletRequest = HttpServletRequestUtils.currentHttpServletRequest();
        final String urlWithModifyStateParameter = modifyHdivStateUrl(url, httpServletRequest);
        LOGGER.debug("Adding modifyHdivStateParameter to url {} -> {}", url, urlWithModifyStateParameter);

        return linkUrlProcessor().processUrl(httpServletRequest, urlWithModifyStateParameter);
    }

    private String modifyHdivStateUrl(final String url, final HttpServletRequest httpServletRequest) {
        final String modifyStateHdivParameter = (String) httpServletRequest.getSession().getAttribute(Constants.MODIFY_STATE_HDIV_PARAMETER);
        final String hdivState = getCsrf();
        final String separator = url.contains("?")? "&" : "?";

        LOGGER.debug("modifyHdivStateParameter: {}, hdivState: {}", modifyStateHdivParameter, hdivState);
        if (StringUtils.isNotBlank(hdivState)) {
            return String.format("%s%s%s=%s", url, separator, modifyStateHdivParameter, hdivState);
        }
        return url;
    }

    private LinkUrlProcessor linkUrlProcessor() {
        return HDIVUtil.getLinkUrlProcessor(servletContext());
    }

    private ServletContext servletContext() {
        return this.servletContext;
    }

    @Override
    public String getCsrf() {

        if (this.csrf == null) {
            /* Add new state to the response */
            /* Only necessary for 'cipher' or 'hash' strategy */
            if (dataComposer() instanceof DataComposerHash || dataComposer() instanceof DataComposerCipher) {
                this.csrf = dataComposer().endRequest();
            } else {
                final HttpServletRequest httpServletRequest = HttpServletRequestUtils.currentHttpServletRequest();
                final Object formStateId= httpServletRequest.getAttribute(FormUrlProcessor.FORM_STATE_ID);
                this.csrf= (String) formStateId;
/*
                String hdivStateParamName = (String) httpServletRequest.getSession().getAttribute(Constants.HDIV_PARAMETER);
                this.csrf = httpServletRequest.getParameter(hdivStateParamName);
*/
            }
        }
        return this.csrf;
    }
}
