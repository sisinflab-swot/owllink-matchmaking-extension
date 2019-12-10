package it.poliba.sisinflab.swottools.owllinkmatchmaking.server.parser;

import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request.ExpressionOrIndividual;
import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request.ExpressionOrIndividualImpl;
import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request.KBRequestWithTwoExpressionOrIndividuals;
import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request.KBRequestWithTwoExpressionsOrIndividualsImpl;
import org.coode.owlapi.owlxmlparser.AbstractClassExpressionElementHandler;
import org.coode.owlapi.owlxmlparser.OWLIndividualElementHandler;
import org.coode.owlapi.owlxmlparser.OWLXMLParserException;
import org.coode.owlapi.owlxmlparser.OWLXMLParserHandler;
import org.semanticweb.owlapi.owllink.builtin.requests.AbstractKBRequestWithTwoObjects;
import org.semanticweb.owlapi.owllink.server.parser.AbstractOWLlinkKBRequestElementHandler;

public class OWLlinkGetNonStandardInferenceSingleElementHandler extends AbstractOWLlinkKBRequestElementHandler<AbstractKBRequestWithTwoObjects> {
    protected ExpressionOrIndividual resource;
    protected ExpressionOrIndividual request;
    protected KBRequestWithTwoExpressionOrIndividuals.RequestType requestType;

    public OWLlinkGetNonStandardInferenceSingleElementHandler(OWLXMLParserHandler handler, KBRequestWithTwoExpressionOrIndividuals.RequestType requestType) {
        super(handler);
        this.requestType = requestType;
    }

    public void startElement(String name) throws OWLXMLParserException {
        super.startElement(name);
        this.resource = null;
        this.request = null;
    }


    public void handleChild(OWLIndividualElementHandler handler) throws OWLXMLParserException {
        if (this.resource == null)
            this.resource = new ExpressionOrIndividualImpl(handler.getOWLObject());
        else
            this.request = new ExpressionOrIndividualImpl(handler.getOWLObject());
    }

    public void handleChild(AbstractClassExpressionElementHandler handler) throws OWLXMLParserException {
        if (this.resource == null)
            this.resource = new ExpressionOrIndividualImpl(handler.getOWLObject());
        else
            this.request = new ExpressionOrIndividualImpl(handler.getOWLObject());
    }

   /* public void handleChild(OWLAnonymousIndividualElementHandler handler) throws OWLXMLParserException {
        if (this.source == null)
            this.source = handler.getOWLObject();
        else
            this.target = handler.getOWLObject();
    }*/

    public KBRequestWithTwoExpressionsOrIndividualsImpl getOWLObject() throws OWLXMLParserException {
        return new KBRequestWithTwoExpressionsOrIndividualsImpl(getKB(), resource, request, requestType);
    }
}