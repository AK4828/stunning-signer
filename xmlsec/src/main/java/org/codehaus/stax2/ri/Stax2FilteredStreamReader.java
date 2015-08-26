package org.codehaus.stax2.ri;



import org.codehaus.stax2.*;
import org.codehaus.stax2.util.StreamReader2Delegate;

import toolkit.xml.stream.StreamFilter;
import toolkit.xml.stream.XMLStreamConstants;
import toolkit.xml.stream.XMLStreamException;
import toolkit.xml.stream.XMLStreamReader;

/**
 * Simple straight-forward implementation of a filtering stream reader,
 * which can fully adapt Stax2 stream reader 
 * ({@link XMLStreamReader2}).
 */
public class Stax2FilteredStreamReader
    extends StreamReader2Delegate
    implements XMLStreamConstants
{
    final StreamFilter mFilter;

    public Stax2FilteredStreamReader(XMLStreamReader r, StreamFilter f)
    {
        super(Stax2ReaderAdapter.wrapIfNecessary(r));
        mFilter = f;
    }

    /*
    //////////////////////////////////////////////////////
    // XMLStreamReader method overrides that we need
    //////////////////////////////////////////////////////
     */

    public int next()
        throws XMLStreamException
    {
        int type;
        do {
            type = mDelegate2.next();
            if (mFilter.accept(this)) {
                break;
            }
        } while (type != END_DOCUMENT);

        return type;
    }

    public int nextTag()
        throws XMLStreamException
    {
        int type;
        // Can be implemented very much like next()
        while (true) {
            type = mDelegate2.nextTag();
            if (mFilter.accept(this)) {
                break;
            }
        }
        return type;
    }
}

