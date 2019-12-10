package it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request;

import org.semanticweb.owlapi.owllink.builtin.response.SetOfIndividuals;

public class SetOfIndividualsAndClassExpressionImpl implements SetOfIndividualsAndClassExpression {

    private ExpressionOrIndividual expressionOrIndividual;
    private SetOfIndividuals setOfOWLNamedIndividuals;

    public SetOfIndividualsAndClassExpressionImpl(ExpressionOrIndividual expressionOrIndividual, SetOfIndividuals setOfOWLNamedIndividuals) {
        this.expressionOrIndividual = expressionOrIndividual;
        this.setOfOWLNamedIndividuals = setOfOWLNamedIndividuals;
    }

    @Override
    public ExpressionOrIndividual getExpressionOrIndividual() {
        return this.expressionOrIndividual;
    }

    @Override
    public SetOfIndividuals getOWLNamedIndividualSet() {
        return this.setOfOWLNamedIndividuals;
    }
}
