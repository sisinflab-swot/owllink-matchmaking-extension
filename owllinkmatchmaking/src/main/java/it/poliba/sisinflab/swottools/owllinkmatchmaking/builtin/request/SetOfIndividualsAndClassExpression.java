package it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request;

import org.semanticweb.owlapi.owllink.builtin.response.SetOfIndividuals;

public interface SetOfIndividualsAndClassExpression {
    ExpressionOrIndividual getExpressionOrIndividual();

    SetOfIndividuals getOWLNamedIndividualSet();
}
