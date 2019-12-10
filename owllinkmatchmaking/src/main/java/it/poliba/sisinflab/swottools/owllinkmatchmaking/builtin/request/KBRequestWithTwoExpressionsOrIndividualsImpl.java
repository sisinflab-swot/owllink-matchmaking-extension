package it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request;

import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.response.ClassExpressionWithNorm;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.owllink.builtin.requests.AbstractKBRequestWithTwoObjects;
import org.semanticweb.owlapi.owllink.builtin.requests.RequestVisitor;

public class KBRequestWithTwoExpressionsOrIndividualsImpl extends AbstractKBRequestWithTwoObjects<ClassExpressionWithNorm,
        ExpressionOrIndividual> implements KBRequestWithTwoExpressionOrIndividuals {
    private RequestType requestType;


    public KBRequestWithTwoExpressionsOrIndividualsImpl(IRI kb, ExpressionOrIndividual resource, ExpressionOrIndividual request, RequestType requestType) {
        super(kb, request, resource);

        this.requestType = requestType;
    }

    @Override
    public ExpressionOrIndividual getResource() {
        return super.firstObject;
    }

    @Override
    public ExpressionOrIndividual getRequest() {
        return super.secondObject;
    }

    @Override
    public RequestType getRequestType() {
        return this.requestType;
    }

    @Override
    public void accept(RequestVisitor visitor) {
        visitor.answer(this);
    }
}
