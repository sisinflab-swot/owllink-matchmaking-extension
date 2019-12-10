package it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request;

public interface KBRequestWithTwoExpressionOrIndividuals {
    ExpressionOrIndividual getResource();

    ExpressionOrIndividual getRequest();

    RequestType getRequestType();

    enum RequestType {
        GET_ABDUCTION, GET_BONUS, GET_DIFFERENCE, GET_CONTRACTION
    }
}
