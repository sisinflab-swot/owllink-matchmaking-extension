package it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.response;

import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.owllink.builtin.response.ResponseVisitor;

public class PairOfClassExpressionsWithNormImpl implements PairOfClassExpressionsWithNorm {


    protected OWLClassExpression owlClassExpressionSecond;
    protected OWLClassExpression owlClassExpressionFirst;
    private Norm norm;
    private String warning;

    public PairOfClassExpressionsWithNormImpl(OWLClassExpression owlClassExpressionFirst, OWLClassExpression owlClassExpressionSecond, Norm norm) {
        this(owlClassExpressionFirst, owlClassExpressionSecond, norm, null);
    }

    public PairOfClassExpressionsWithNormImpl(OWLClassExpression owlClassExpressionFirst, OWLClassExpression owlClassExpressionSecond, Norm norm, String warning) {
        this.warning = warning;
        this.norm = norm;
        this.owlClassExpressionSecond = owlClassExpressionSecond;
        this.owlClassExpressionFirst = owlClassExpressionFirst;
    }

    @Override
    public OWLClassExpression getOWLClassExpressionFirst() {
        return this.owlClassExpressionFirst;
    }

    @Override
    public OWLClassExpression getOWLClassExpressionSecond() {
        return this.owlClassExpressionSecond;
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
}
