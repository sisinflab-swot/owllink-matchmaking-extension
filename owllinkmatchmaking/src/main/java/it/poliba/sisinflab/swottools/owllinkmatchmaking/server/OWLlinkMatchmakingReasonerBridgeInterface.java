package it.poliba.sisinflab.swottools.owllinkmatchmaking.server;

import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request.GetCovering;
import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request.KBRequestWithTwoExpressionOrIndividuals;

public interface OWLlinkMatchmakingReasonerBridgeInterface {
    void getAbduction(KBRequestWithTwoExpressionOrIndividuals request);
    void getBonus(KBRequestWithTwoExpressionOrIndividuals request);
    void getDifference(KBRequestWithTwoExpressionOrIndividuals request);
    void getContraction(KBRequestWithTwoExpressionOrIndividuals request);
    void getCovering(GetCovering request);
}
