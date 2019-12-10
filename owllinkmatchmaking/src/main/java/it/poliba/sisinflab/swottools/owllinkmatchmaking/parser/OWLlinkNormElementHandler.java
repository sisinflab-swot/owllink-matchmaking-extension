package it.poliba.sisinflab.swottools.owllinkmatchmaking.parser;

import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.response.Norm;
import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.response.NormImpl;
import org.coode.owlapi.owlxmlparser.OWLXMLParserException;
import org.coode.owlapi.owlxmlparser.OWLXMLParserHandler;
import org.semanticweb.owlapi.io.OWLParserException;
import org.semanticweb.owlapi.model.UnloadableImportException;
import org.semanticweb.owlapi.owllink.parser.AbstractOWLlinkElementHandler;

public class OWLlinkNormElementHandler extends AbstractOWLlinkElementHandler<Norm> {
    Norm norm;

    public OWLlinkNormElementHandler(OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    public Norm getOWLLinkObject() throws OWLXMLParserException {
        return norm;
    }

    @Override
    public void endElement() throws OWLParserException, UnloadableImportException {
        getParentHandler().handleChild(this);
    }

    public void attribute(String localName, String value) throws OWLXMLParserException {
        super.attribute(localName, value);
        if ("value".equals(localName))
            norm = new NormImpl(Float.parseFloat(value));
    }
}
