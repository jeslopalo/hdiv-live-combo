package es.sandbox.app.web.control;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;


public final class HttpServletRequestUtils { // NO_UCD (test only)

    /**
     * Private constructor to prevent instances
     *
     * @throws UnsupportedOperationException
     */
    private HttpServletRequestUtils() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * @return
     *
     * @throws NotHttpServletRequestBoundToThreadException
     */
    public static HttpServletRequest currentHttpServletRequest() // NO_UCD (use default)
            throws NotHttpServletRequestBoundToThreadException {

        final ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new NotHttpServletRequestBoundToThreadException();
        }

        return attributes.getRequest();
    }

    public static Object getAttributeFromCurrentHttpServletRequest(final String attributeName) {
        return currentHttpServletRequest().getAttribute(attributeName);
    }
}
