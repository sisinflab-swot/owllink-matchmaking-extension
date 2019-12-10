package it.poliba.sisinflab.swottools.owllinkmatchmaking.server.parser;


import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request.ExpressionOrIndividual;
import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request.ExpressionOrIndividualImpl;
import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request.GetCovering;
import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request.SetOfIndividualsAndClassExpressionImpl;
import org.coode.owlapi.owlxmlparser.AbstractClassExpressionElementHandler;
import org.coode.owlapi.owlxmlparser.OWLIndividualElementHandler;
import org.coode.owlapi.owlxmlparser.OWLXMLParserException;
import org.coode.owlapi.owlxmlparser.OWLXMLParserHandler;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfIndividuals;
import org.semanticweb.owlapi.owllink.server.parser.AbstractOWLlinkKBRequestElementHandler;
import org.semanticweb.owlapi.owllink.server.parser.OWLlinkRequestElementHandler;

public class OWLlinkGetCoveringSingleElementHandler extends AbstractOWLlinkKBRequestElementHandler<GetCovering> {
    protected ExpressionOrIndividual request;
    protected SetOfIndividuals setOfIndividuals;

    public OWLlinkGetCoveringSingleElementHandler(OWLXMLParserHandler handler) {
        super(handler);

    }

    public void startElement(String name) throws OWLXMLParserException {
        super.startElement(name);
        this.setOfIndividuals = null;
        this.request = null;
    }


    public void handleChild(OWLIndividualElementHandler handler) throws OWLXMLParserException {

        this.request = new ExpressionOrIndividualImpl(handler.getOWLObject());
    }

    public void handleChild(AbstractClassExpressionElementHandler handler) throws OWLXMLParserException {

        this.request = new ExpressionOrIndividualImpl(handler.getOWLObject());
    }

    public void handleChild(OWLlinkRequestElementHandler handler) throws OWLXMLParserException {
        this.setOfIndividuals = (SetOfIndividuals) handler.getOWLlinkObject();
    }

   /* public void handleChild(OWLAnonymousIndividualElementHandler handler) throws OWLXMLParserException {
        if (this.source == null)
            this.source = handler.getOWLObject();
        else
            this.target = handler.getOWLObject();
    }*/

    public GetCovering getOWLObject() throws OWLXMLParserException {
        return new GetCovering(getKB(), new SetOfIndividualsAndClassExpressionImpl(request, setOfIndividuals));
    }
}