package org.codehaus.stax2.osgi;

import org.codehaus.stax2.XMLOutputFactory2;
import org.codehaus.stax2.XMLStreamProperties;

import toolkit.xml.stream.XMLOutputFactory;

/**
 * Simple interface to be used for registering objects that
 * can construct {@link XMLOutputFactory2} instances with OSGi framework.
 * The added indirection (provider constructing factory) is needed because
 * of impedance between OSGi service objects (which are essentially
 * singletons) and Stax/Stax2 factories which are not.
 *<p>
 * Note: implementations of provider should <b>NOT</b> use introspection
 * via {@link XMLOutputFactory#newInstance} as it will
 * not work with OSGi. Instead, providers should directly construct
 * instances of concrete factory they represent. That is, there will
 * be one provider implementation per concrete Stax/Stax2 implementation
 */
public interface Stax2OutputFactoryProvider
{
    /*
    ///////////////////////////////////////////////////////////////
    // Service property names to use with the provider
    ///////////////////////////////////////////////////////////////
     */

    /**
     * Service property that defines name of Stax2 implementation that
     * this provider represents.
     */
    public final static String OSGI_SVC_PROP_IMPL_NAME = XMLStreamProperties.XSP_IMPLEMENTATION_NAME;

    /**
     * Service property that defines version of Stax2 implementation that
     * this provider represents.
     */
    public final static String OSGI_SVC_PROP_IMPL_VERSION = XMLStreamProperties.XSP_IMPLEMENTATION_VERSION;

    /*
    ///////////////////////////////////////////////////////////////
    // Public provider API
    ///////////////////////////////////////////////////////////////
     */

    /**
     * Method called to create a new {@link XMLOutputFactory2} instance.
     *
     * @return Output factory configured to implementation-specific
     *   default settings (some of which are mandated by Stax and Stax2
     *   specifications)
     */
    public XMLOutputFactory2 createOutputFactory();
}
