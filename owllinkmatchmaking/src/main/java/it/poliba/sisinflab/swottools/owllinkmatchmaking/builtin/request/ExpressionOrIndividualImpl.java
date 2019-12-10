package it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request;

import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObject;

public class ExpressionOrIndividualImpl implements ExpressionOrIndividual {
    private OWLObject descriptionOrIndividual;

    public ExpressionOrIndividualImpl(OWLNamedIndividual descriptionOrIndividual) {
        this.descriptionOrIndividual = descriptionOrIndividual;
    }

    public ExpressionOrIndividualImpl(OWLClassExpression descriptionOrIndividual) {
        this.descriptionOrIndividual = descriptionOrIndividual;
    }

    @Override
    public OWLNamedIndividual getOWLNamedIndividual() {
        return (OWLNamedIndividual) this.descriptionOrIndividual;
    }

    @Override
    public OWLClassExpression getOWLClassExpression() {
        return (OWLClassExpression) this.descriptionOrIndividual;
    }

    @Override
    public boolean isOWLNamedIndividual() {
        return (descriptionOrIndividual instanceof OWLNamedIndividual);
    }

    @Override
    public boolean isOWLClassExpression() {
        return (descriptionOrIndividual instanceof OWLClassExpression);
    }

    @Override
    public OWLObject getOWLObject() {
        return descriptionOrIndividual;
    }
}
