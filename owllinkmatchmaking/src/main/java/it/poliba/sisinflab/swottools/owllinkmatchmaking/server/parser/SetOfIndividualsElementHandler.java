package it.poliba.sisinflab.swottools.owllinkmatchmaking.server.parser;

import org.coode.owlapi.owlxmlparser.OWLAnonymousIndividualElementHandler;
import org.coode.owlapi.owlxmlparser.OWLIndividualElementHandler;
import org.coode.owlapi.owlxmlparser.OWLXMLParserException;
import org.coode.owlapi.owlxmlparser.OWLXMLParserHandler;
import org.semanticweb.owlapi.io.OWLParserException;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfIndividualsImpl;
import org.semanticweb.owlapi.owllink.server.parser.AbstractOWLlinkKBRequestElementHandler;
import org.semanticweb.owlapi.util.CollectionFactory;

import java.util.Set;

public class SetOfIndividualsElementHandler extends AbstractOWLlinkKBRequestElementHandler {

    private Set<OWLIndividual> elements;

    public SetOfIndividualsElementHandler(OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    public void startElement(String name) throws OWLXMLParserException {
        super.startElement(name);
        elements = CollectionFactory.createSet();
    }

    @Override
    public void endElement() throws OWLParserException {
        getParentHandler().handleChild(this);
    }

    @Override
    public Object getOWLObject() throws OWLXMLParserException {
        return new SetOfIndividualsImpl(this.elements);
    }

    public void handleChild(OWLIndividualElementHandler handler) throws OWLXMLParserException {
        this.elements.add(handler.getOWLObject());
    }

    public void handleChild(OWLAnonymousIndividualElementHandler handler) throws OWLXMLParserException {
        this.elements.add(handler.getOWLObject());
    }
}
