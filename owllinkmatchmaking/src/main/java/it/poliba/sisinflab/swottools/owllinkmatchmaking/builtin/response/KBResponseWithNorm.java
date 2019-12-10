package it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.response;

import org.semanticweb.owlapi.owllink.builtin.response.KBResponse;

public interface KBResponseWithNorm extends KBResponse {
    Norm getNorm();
}
