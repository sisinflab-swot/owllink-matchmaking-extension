package it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.response;

import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.owllink.builtin.response.ResponseVisitor;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfIndividuals;

public class SetOfIndividualsWithClassExpressionAndNormImpl implements SetOfIndividualsWithClassExpressionAndNorm {
    private OWLClassExpression owlClassExpression;
    private Norm norm;
    private SetOfIndividuals setOfOWLNamedIndividuals;
    private String warning;

    public SetOfIndividualsWithClassExpressionAndNormImpl(SetOfIndividuals setOfOWLNamedIndividual, OWLClassExpression owlClassExpression, Norm norm, String warning) {
        this.warning = warning;
        this.norm = norm;
        this.setOfOWLNamedIndividuals = setOfOWLNamedIndividual;
        this.owlClassExpression = owlClassExpression;
    }

    public SetOfIndividualsWithClassExpressionAndNormImpl(SetOfIndividuals setOfOWLNamedIndividual, OWLClassExpression owlClassExpression, Norm norm) {
        this(setOfOWLNamedIndividual, owlClassExpression, norm, null);

    }

    @Override
    public Norm getNorm() {
        return this.norm;
    }

    @Override
    public boolean hasWarning() {
        return this.warning != null;
    }

    @Override
    public String getWarning() {
        return this.warning;
    }

    @Override
    public <O> O accept(ResponseVisitor<O> visitor) {
        return visitor.visit(this);
    }

    @Override
    public SetOfIndividuals getOWLNamedIndividualSet() {
        return this.setOfOWLNamedIndividuals;
    }

    @Override
    public OWLClassExpression getOwlClassExpression() {
        return this.owlClassExpression;
    }
}
