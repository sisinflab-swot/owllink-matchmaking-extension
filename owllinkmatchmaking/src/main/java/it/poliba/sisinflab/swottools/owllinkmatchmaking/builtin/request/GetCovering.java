package it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request;

import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.response.SetOfIndividualsWithClassExpressionAndNorm;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.owllink.builtin.requests.AbstractKBRequestWithOneObject;
import org.semanticweb.owlapi.owllink.builtin.requests.RequestVisitor;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfIndividuals;

public class GetCovering extends AbstractKBRequestWithOneObject<SetOfIndividualsWithClassExpressionAndNorm,
        SetOfIndividualsAndClassExpression> {


    public GetCovering(IRI kb, SetOfIndividualsAndClassExpression setOfIndividualsAndClassExpression) {
        super(kb, setOfIndividualsAndClassExpression);
    }


    public SetOfIndividuals getSetOfIndividuals() {
        return super.getObject().getOWLNamedIndividualSet();
    }

    public ExpressionOrIndividual getRequest() {
        return super.getObject().getExpressionOrIndividual();
    }

    @Override
    public void accept(RequestVisitor visitor) {
        visitor.answer(this);
    }
}