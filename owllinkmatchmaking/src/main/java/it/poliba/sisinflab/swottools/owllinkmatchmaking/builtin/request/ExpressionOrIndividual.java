package it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request;

import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObject;

public interface ExpressionOrIndividual {

    OWLNamedIndividual getOWLNamedIndividual();

    OWLClassExpression getOWLClassExpression();

    boolean isOWLNamedIndividual();

    boolean isOWLClassExpression();

    OWLObject getOWLObject();
}
