package it.poliba.sisinflab.swottools.owllinkmatchmaking;

import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request.ExpressionOrIndividual;
import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request.KBRequestWithTwoExpressionOrIndividuals;
import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request.KBRequestWithTwoExpressionsOrIndividualsImpl;
import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.response.ClassExpressionWithNorm;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.owllink.OWLlinkHTTPXMLReasoner;
import org.semanticweb.owlapi.owllink.OWLlinkReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.BufferingMode;

public class OWLlinkMatchmakingHTTPXMLReasoner extends OWLlinkHTTPXMLReasoner {
    public OWLlinkMatchmakingHTTPXMLReasoner(OWLOntology rootOntology, OWLlinkReasonerConfiguration configuration, BufferingMode bufferingMode) {
        super(rootOntology, configuration, bufferingMode);
        session = new HTTPSessionImplMatchmaking(rootOntology.getOWLOntologyManager(), configuration.getReasonerURL(), prov);
    }

    public ClassExpressionWithNorm getAbduction(ExpressionOrIndividual request, ExpressionOrIndividual resource) {
        KBRequestWithTwoExpressionsOrIndividualsImpl query = new KBRequestWithTwoExpressionsOrIndividualsImpl(defaultKnowledgeBase, request, resource, KBRequestWithTwoExpressionOrIndividuals.RequestType.GET_ABDUCTION);
        return performRequest(query);
    }

    public ClassExpressionWithNorm getDifference(ExpressionOrIndividual request, ExpressionOrIndividual resource) {
        KBRequestWithTwoExpressionsOrIndividualsImpl query = new KBRequestWithTwoExpressionsOrIndividualsImpl(defaultKnowledgeBase, request, resource, KBRequestWithTwoExpressionOrIndividuals.RequestType.GET_DIFFERENCE);
        return performRequest(query);
    }


}
