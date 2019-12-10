/*
 * This file is part of the OWLlink API.
 *
 * The contents of this file are subject to the LGPL License, Version 3.0.
 *
 * Copyright (C) 2011, derivo GmbH
 * Copyright (c) 2019 SisInf Lab, Polytechnic University of Bari
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0
 * in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 *
 * Copyright 2011, derivo GmbH
 * Copyright (c) 2019 SisInf Lab, Polytechnic University of Bari
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.semanticweb.owlapi.owllink.server;

import org.mortbay.http.HttpRequest;
import org.mortbay.http.HttpResponse;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.owllink.DefaultPrefixManagerProvider;
import org.semanticweb.owlapi.owllink.Request;
import org.semanticweb.owlapi.owllink.Response;
import org.semanticweb.owlapi.owllink.builtin.requests.*;
import org.semanticweb.owlapi.owllink.builtin.response.*;
import org.semanticweb.owlapi.owllink.retraction.RetractRequest;
import org.semanticweb.owlapi.owllink.server.parser.OWLLinkRequestListener;
import org.semanticweb.owlapi.owllink.server.parser.OWLlinkXMLRequestParserHandler;
import org.semanticweb.owlapi.owllink.server.renderer.OWLlinkXMLResponseRenderer;
import org.semanticweb.owlapi.owllink.server.response.*;
import org.semanticweb.owlapi.reasoner.*;
import org.semanticweb.owlapi.reasoner.impl.*;
import org.semanticweb.owlapi.util.CollectionFactory;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.Namespaces;
import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * The <code>OWLlinkReasonerBridge</code> mediates between OWLlink and an implementation of OWLReasoner.
 * It supports the core OWLlink queries (as can be simulated via the OWL API) as well as the retraction extension.
 * <p/>
 * Author: Olaf Noppens
 * Date: 25.10.2009
 */
public class OWLlinkReasonerBridge implements RequestVisitor {

    protected OWLOntologyManager manager;
    protected OWLReasonerFactory factory;
    protected Map<IRI, OWLReasoner> reasonersByKB;
    private OWLlinkReasonerConfiguration reasonerConfiguration;
    protected Response response;
    final OWLObjectProperty topObjectProperty;
    final OWLObjectProperty bottomObjectProperty;
    final OWLDataProperty topDataProperty;
    final OWLDataProperty bottomDataProperty;
    private Map<IRI, String> kbNameByIRI;
    private Map<IRI, AbstractOWLlinkReasonerConfiguration> configurationsByKB;
    private Map<IRI, Stack<String>> warningsByReasoners;


    public static class BlockablePrefixManagerProvider extends DefaultPrefixManagerProvider {
        private Set<IRI> blockedKBs;

        public BlockablePrefixManagerProvider() {
            this.blockedKBs = CollectionFactory.createSet();
        }

        public void setBlocked(IRI kb, boolean isBlocked) {
            if (isBlocked)
                blockedKBs.add(kb);
            else
                blockedKBs.remove(kb);
        }

        @Override
        public boolean contains(IRI knowledgeBase) {
            if (blockedKBs.contains(knowledgeBase))
                return false;
            return super.contains(knowledgeBase);
        }

        @Override
        public PrefixManager getPrefixes(IRI knowledgeBase) {
            if (blockedKBs.contains(knowledgeBase)) return null;
            return super.getPrefixes(knowledgeBase);
        }

        @Override
        public void removePrefixes(IRI knowledgeBase) {
            super.removePrefixes(knowledgeBase);
            blockedKBs.remove(knowledgeBase);
        }
    }


    protected BlockablePrefixManagerProvider prov = new BlockablePrefixManagerProvider();

    public OWLlinkReasonerBridge(OWLReasonerFactory factory, OWLlinkReasonerConfiguration configuration) {
        this.reasonersByKB = CollectionFactory.createMap();
        this.manager = OWLManager.createOWLOntologyManager();
        this.factory = factory;
        this.reasonerConfiguration = configuration;
        this.topObjectProperty = manager.getOWLDataFactory().getOWLTopObjectProperty();
        this.bottomObjectProperty = manager.getOWLDataFactory().getOWLBottomObjectProperty();
        this.topDataProperty = manager.getOWLDataFactory().getOWLTopDataProperty();
        this.bottomDataProperty = manager.getOWLDataFactory().getOWLBottomDataProperty();
        this.kbNameByIRI = CollectionFactory.createMap();
        this.configurationsByKB = CollectionFactory.createMap();
        this.warningsByReasoners = CollectionFactory.createMap();
    }


    public final synchronized void process(HttpRequest request, HttpResponse response) throws SAXException, IOException {
        String field = request.getField("Accept-Encoding");
        boolean clientAcceptGzip = false;
        boolean clientContentIsGzip = false;
        if (field != null) {
            StringTokenizer tokenizer = new StringTokenizer(field, ",");
            while (tokenizer.hasMoreTokens()) {
                if ("gzip".equals(tokenizer.nextToken())) {
                    //we can gzip our result
                    clientAcceptGzip = true;
                    break;
                }
            }
        }
        field = request.getField("content-encoding");
        if (field != null) {
            StringTokenizer tokenizer = new StringTokenizer(field, ",");
            while (tokenizer.hasMoreTokens()) {
                if ("gzip".equals(tokenizer.nextToken())) {
                    //inputStream must be a GZIPInputStream
                    clientContentIsGzip = true;
                    break;
                }
            }
        }
        response.setField("Accept-Encoding", "gzip");
        this.process(clientContentIsGzip ? new GZIPInputStream(response.getInputStream()) : response.getInputStream(), response.getOutputStream(), response, clientAcceptGzip);

    }


    public synchronized boolean process(InputStream in, OutputStream out, HttpResponse response, boolean zipContentIfAppropriate) throws SAXException, IOException {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();

            factory.setNamespaceAware(true);
            SAXParser parser = factory.newSAXParser();
            OWLOntology ontology = manager.createOntology();
            final OWLlinkXMLRequestParserHandler handler = new OWLlinkXMLRequestParserHandler(ontology, prov, null);
            final List<Response> responses = new ArrayList<Response>();

            final OWLLinkRequestListener listener = new OWLLinkRequestListener() {
                public void requestAdded(Request request) {
                    try {
                        request.accept(OWLlinkReasonerBridge.this);
                    } catch (Exception e) {
                        handle(e);
                    }
                    responses.add(getResponse());
                }
            };
            handler.setRequestListener(listener);
            parser.parse(in, handler);

            boolean useCompression = false;
            if (responses.size() > 20 && zipContentIfAppropriate) {
                useCompression = true;
                response.setField("content-encoding", "gzip");
            }
            GZIPOutputStream gzipOut = useCompression ? new GZIPOutputStream(out) : null;
            OutputStreamWriter writer = new OutputStreamWriter(gzipOut == null ? out : gzipOut);
            OWLlinkXMLResponseRenderer renderer = new OWLlinkXMLResponseRenderer();
            List<Request> requests = handler.getRequest();
            renderer.render(writer, prov, requests, responses);
            writer.flush();
            if (gzipOut != null)
                gzipOut.finish();

            return zipContentIfAppropriate;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public final OWLReasoner getReasoner(IRI kb) throws KBException {
        final OWLReasoner reasoner = this.reasonersByKB.get(kb);
        if (reasoner == null)
            throw new KBException("KB " + kb.toString() + " does not exist!");
        return reasoner;
    }

    public final OWLlinkReasonerConfiguration getReasonerConfiguration(IRI kb, boolean createIfNull) throws KBException {
        OWLlinkReasonerConfiguration configuration = this.configurationsByKB.get(kb);
        if (configuration == null && createIfNull) {
            configuration = new AbstractOWLlinkReasonerConfiguration(this.reasonerConfiguration);
        }
        return configuration;
    }

    public final void logWarning(OWLReasoner reasoner, String warning) {
        Stack<String> warnings = this.warningsByReasoners.get(reasoner);
        if (warnings == null) {
            warnings = new Stack<String>();
        }
        warnings.push(warning);
    }

    protected OWLOntologyManager getOntologyManager(IRI kb) {
        return this.manager;
    }

    protected String getWarning(OWLReasoner reasoner) {
        {
            Stack<String> warnings = this.warningsByReasoners.get(reasoner);
            if (warnings == null) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            for (String s : warnings)
                sb.append(s);
            this.warningsByReasoners.remove(reasoner);
            return sb.toString();
        }
    }

    class KBException extends RuntimeException {

        public KBException(String message) {
            super(message);
        }
    }

    protected ErrorResponse handle(Exception e) {
        if (e instanceof InconsistentOntologyException) {
            this.response = new UnsatisfiableKBErrorResponseImpl(e.getMessage() == null ? e.toString() : e.getMessage());
        } else if (e instanceof AxiomNotInProfileException) {
            StringBuilder errorString = new StringBuilder();
            if (((AxiomNotInProfileException) e).getAxiom() != null)
                errorString.append("axiom: " + ((AxiomNotInProfileException) e).getAxiom());
            if (((AxiomNotInProfileException) e).getProfile() != null)
                errorString.append("profile: " + ((AxiomNotInProfileException) e).getProfile().getName());
            this.response = new ProfileViolationErrorResponseImpl(errorString.toString().isEmpty() ? e.toString() : errorString.toString());
        } else if (e instanceof KBException) {
            this.response = new KBErrorResponseImpl(e.getMessage() == null ? e.toString() : e.getMessage());
        } else
            this.response = new ErrorResponseImpl(e.getMessage() == null ? e.toString() : e.getMessage());
        return (ErrorResponse) this.response;
    }

    protected ErrorResponse handleReasonerException(Exception e) {
        if (e instanceof KBException) {
            this.response = new KBErrorResponseImpl(e.getMessage() == null ? e.toString() : e.getMessage());
        } else
            this.response = new ErrorResponseImpl(factory.getReasonerName() + ": " + (e.getMessage() == null ? e.toString() : e.getMessage()));
        return (ErrorResponse) this.response;
    }

    public void answer(Classify query) {
        OWLReasoner reasoner = getReasoner(query.getKB());
        reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
        this.response = new OKImpl();
    }

    public void answer(Realize query) {
        OWLReasoner reasoner = getReasoner(query.getKB());
        reasoner.precomputeInferences(InferenceType.CLASS_ASSERTIONS);
        //reasoner.prepareReasoner();
        this.response = new OKImpl();
    }

    public void answer(CreateKB query) {
        IRI kb = query.getKB();
        if (kb != null) {
            OWLReasoner reasoner = this.reasonersByKB.get(query.getKB());
            if (reasoner != null)
                throw new OWLlinkErrorResponseException("KB " + query.getKB() + " already exists!");
        } else
            kb = IRI.create("http://owllink.org#" + getClass().getName() + ".kb" + System.currentTimeMillis());
        OWLOntology ontology = null;
        try {
            ontology = manager.createOntology(kb);
        } catch (OWLOntologyCreationException e) {
        }
        AbstractOWLlinkReasonerConfiguration configuration = (AbstractOWLlinkReasonerConfiguration) getReasonerConfiguration(kb, true);
        this.configurationsByKB.put(kb, configuration);
        OWLReasoner reasoner;
        if (configuration.getOWLReasonerConfiguration() == null)
            reasoner = factory.createNonBufferingReasoner(ontology);
        else
            reasoner = factory.createNonBufferingReasoner(ontology, configuration.getOWLReasonerConfiguration());

        if (reasoner instanceof OWLOntologyChangeListener) {
            //because some reasoners (FaCt++ for example) do not do this task on their own....:-(
             manager.removeOntologyChangeListener((OWLOntologyChangeListener) reasoner);
             manager.addOntologyChangeListener((OWLOntologyChangeListener) reasoner);
        }
        DefaultPrefixManager manager = new DefaultPrefixManager();
        manager.clear();
        manager.setPrefix("owl:", Namespaces.OWL.toString());
        manager.setPrefix("xsd:", Namespaces.XSD.toString());
        manager.setPrefix("rdf:", Namespaces.RDF.toString());
        manager.setPrefix("rdfs:", Namespaces.RDFS.toString());

        Map<String, String> map = CollectionFactory.createMap();
        Map<String, String> prefixes = query.getPrefixes();
        if (prefixes != null) {
            for (Map.Entry<String, String> prefix : prefixes.entrySet()) {
                if (!prefix.getKey().endsWith(":"))
                    map.put(prefix.getKey() + ":", prefix.getValue().toString());
                else
                    map.put(prefix.getKey(), prefix.getValue().toString());
            }
        }
        for (Map.Entry<String, String> entry : map.entrySet())
            manager.setPrefix(entry.getKey(), entry.getValue());
        prov.putPrefixes(kb, manager);

        if (query.getName() != null) {
            this.kbNameByIRI.put(kb, query.getName());
        }


        this.reasonersByKB.put(kb, reasoner);
        this.response = new KBImpl(kb);
    }

    public void answer(GetAllAnnotationProperties query) {
        Set<OWLAnnotationProperty> properties = CollectionFactory.createSet();
        OWLReasoner reasoner = getReasoner(query.getKB());

        for (OWLOntology ontology : reasoner.getRootOntology().getImportsClosure()) {
            properties.addAll(ontology.getAnnotationPropertiesInSignature());
        }
        this.response = new SetOfAnnotationPropertiesImpl(properties, getWarning(reasoner));
    }

    public void answer(GetAllClasses query) {
        OWLReasoner reasoner = getReasoner(query.getKB());
        java.util.Set<OWLClass> classes = CollectionFactory.createSet();
        for (OWLOntology ontology : reasoner.getRootOntology().getImportsClosure()) {
            classes.addAll(ontology.getClassesInSignature());
        }
        this.response = new SetOfClassesImpl(classes);
    }

    public void answer(GetAllDataProperties query) {
        OWLReasoner reasoner = getReasoner(query.getKB());

        java.util.Set<OWLDataProperty> properties = CollectionFactory.createSet();
        for (OWLOntology ontology : reasoner.getRootOntology().getImportsClosure()) {
            properties.addAll(ontology.getDataPropertiesInSignature());
        }
        this.response = new SetOfDataPropertiesImpl(properties);
    }

    public void answer(GetAllDatatypes query) {
        OWLReasoner reasoner = getReasoner(query.getKB());
        java.util.Set<OWLDatatype> datatypes = CollectionFactory.createSet();
        for (OWLOntology ontology : reasoner.getRootOntology().getImportsClosure()) {
            for (OWLDatatype type : ontology.getDatatypesInSignature()) {
                datatypes.add(type);
            }
        }
        this.response = new SetOfDatatypesImpl(datatypes);
    }

    public void answer(GetAllIndividuals query) {
        OWLReasoner reasoner = getReasoner(query.getKB());

        java.util.Set<OWLIndividual> individuals = CollectionFactory.createSet();
        for (OWLOntology ontology : reasoner.getRootOntology().getImportsClosure()) {
            individuals.addAll(ontology.getIndividualsInSignature());
        }
        this.response = new SetOfIndividualsImpl(individuals);
    }

    public void answer(GetAllObjectProperties query) {
        OWLReasoner reasoner = getReasoner(query.getKB());
        java.util.Set<OWLObjectProperty> properties = CollectionFactory.createSet();
        for (OWLOntology ontology : reasoner.getRootOntology().getImportsClosure()) {
            properties.addAll(ontology.getObjectPropertiesInSignature());
        }
        this.response = new SetOfObjectPropertiesImpl(properties);
    }

    public void answer(GetDataPropertiesBetween query) {
        if (query.getSourceIndividual().isAnonymous()) {
            this.response = new ErrorResponseImpl("Anonymous source individuals are not allowed");
            return;
        }
        if (query.isNegative()) {
            this.response = new ErrorResponseImpl("(negative=true) in GetDataPropertiesBetween is not supported");
            return;
        }

        OWLReasoner reasoner = getReasoner(query.getKB());
        java.util.Set<OWLDataProperty> properties = CollectionFactory.createSet();
        java.util.Set<Node<OWLDataProperty>> synsetSet = CollectionFactory.createSet();
        boolean topConsidered = false;
        for (OWLDataProperty property : getAllDataProperties(query.getKB())) {
            if (properties.contains(property)) continue;
            java.util.Set<OWLLiteral> literals =
                    reasoner.getDataPropertyValues(query.getSourceIndividual().asOWLNamedIndividual(), property);
            if (literals.contains(query.getTargetValue())) {
                properties.add(property);
                Node<OWLDataProperty> equis = reasoner.getEquivalentDataProperties(property);
                if (!topConsidered)
                    topConsidered = equis.contains(topDataProperty);
                synsetSet.add(equis);
            }
        }

        if (!topConsidered)

        {
            synsetSet.add(new OWLDataPropertyNode(this.topDataProperty));
            this.response = new SetOfDataPropertySynsetsImpl(synsetSet, "Equivalents to TopDataProperty not considered");
        } else this.response = new

                SetOfDataPropertySynsetsImpl(synsetSet, getWarning(reasoner)

        );
    }

    protected java.util.Set<OWLClass> getAllClasses(IRI kb) {
        final OWLReasoner reasoner = getReasoner(kb);
        java.util.Set<OWLClass> set = CollectionFactory.createSet();
        for (OWLOntology ontology : reasoner.getRootOntology().getImportsClosure())
            set.addAll(ontology.getClassesInSignature());

        return set;
    }

    protected java.util.Set<OWLDataProperty> getAllDataProperties(IRI kb) {
        final OWLReasoner reasoner = getReasoner(kb);
        final java.util.Set<OWLDataProperty> set = CollectionFactory.createSet();
        for (OWLOntology ontology : reasoner.getRootOntology().getImportsClosure())
            set.addAll(ontology.getDataPropertiesInSignature());
        return set;
    }

    protected java.util.Set<OWLObjectProperty> getAllObjectProperties(IRI kb) {
        final OWLReasoner reasoner = getReasoner(kb);
        final java.util.Set<OWLObjectProperty> set = CollectionFactory.createSet();
        for (OWLOntology ontology : reasoner.getRootOntology().getImportsClosure())
            set.addAll(ontology.getObjectPropertiesInSignature());
        return set;
    }

    protected java.util.Set<OWLIndividual> getAllIndividuals(IRI kb) {
        final java.util.Set<OWLIndividual> set = CollectionFactory.createSet();
        final OWLReasoner reasoner = getReasoner(kb);
        for (OWLOntology indi : reasoner.getRootOntology().getImportsClosure()) {
            set.addAll(indi.getIndividualsInSignature());
        }
        return set;
    }

    public void answer(GetDataPropertiesOfLiteral query) {
        if (query.isNegative())
            this.response = new ErrorResponseImpl("(negative=true) in GetDataPropertiesOfLiteral is not supported");
        else {
            OWLReasoner reasoner = getReasoner(query.getKB());
            Set<OWLDataProperty> allProps = getAllDataProperties(query.getKB());
            Set<OWLIndividual> allIndis = getAllIndividuals(query.getKB());
            boolean topConsidered = false;
            {
                Set<Node<OWLDataProperty>> answerNodeSet = CollectionFactory.createSet();
                Set<Node<OWLDataProperty>> setOfNodes = CollectionFactory.createSet();
                while (!allProps.isEmpty()) {
                    OWLDataProperty prop = allProps.iterator().next();
                    allProps.remove(prop);
                    Node<OWLDataProperty> equivalents = reasoner.getEquivalentDataProperties(prop);
                    if (equivalents.getSize() > 0) { //should always be the case!
                        setOfNodes.add(equivalents);
                        allProps.removeAll(equivalents.getEntities());
                    }
                }
                for (OWLIndividual indi : allIndis) {
                    if (indi.isAnonymous()) continue;
                    for (Node<OWLDataProperty> node : setOfNodes) {
                        if (reasoner.getDataPropertyValues(indi.asOWLNamedIndividual(), node.getRepresentativeElement()).contains(query.getTargetValue())) {
                            answerNodeSet.add(node);
                        }
                        if (!topConsidered)
                            topConsidered = node.isBottomNode();
                    }
                }
                if (!topConsidered) {
                    answerNodeSet.add(reasoner.getTopDataPropertyNode());
                }
                this.response = new SetOfDataPropertySynsetsImpl(answerNodeSet, getWarning(reasoner));
            }
        }
    }

    public void answer(GetDataPropertiesOfSource query) {
        if (query.isNegative())
            this.response = new ErrorResponseImpl("(negative=true) in GetDataPropertiesOfSource is not supported");
        else if (query.getSourceIndividual().isAnonymous())
            this.response = new ErrorResponseImpl("anonymous individuals are not supported");
        else {
            OWLReasoner reasoner = getReasoner(query.getKB());
            Set<OWLDataProperty> allProps = getAllDataProperties(query.getKB());
            Set<Node<OWLDataProperty>> answerNodeSet = CollectionFactory.createSet();
            Set<Node<OWLDataProperty>> setOfNodes = CollectionFactory.createSet();
            boolean topConsidered = false;
            while (!allProps.isEmpty()) {
                OWLDataProperty prop = allProps.iterator().next();
                allProps.remove(prop);
                Node<OWLDataProperty> equivalents = reasoner.getEquivalentDataProperties(prop);
                if (equivalents.getSize() > 0) { //should always be the case!
                    setOfNodes.add(equivalents);
                    allProps.removeAll(equivalents.getEntities());
                }
                for (Node<OWLDataProperty> node : setOfNodes) {
                    Set<OWLLiteral> literals = reasoner.getDataPropertyValues(
                            query.getSourceIndividual().asOWLNamedIndividual(), node.getRepresentativeElement());
                    if (literals.size() > 0) {
                        answerNodeSet.add(node);
                        if (!topConsidered)
                            topConsidered = node.contains(this.topDataProperty);
                    }
                }
                if (!topConsidered)
                    answerNodeSet.add(reasoner.getTopDataPropertyNode());
                this.response = new SetOfDataPropertySynsetsImpl(answerNodeSet, getWarning(reasoner));
            }
        }
    }

    public void answer(GetDataPropertySources query) {
        if (query.isNegative())
            this.response = new ErrorResponseImpl("(negative=true) in GetDataPropertiesOfSource is not supported");
        else {
            String warning = null;
            OWLReasoner reasoner = getReasoner(query.getKB());
            Set<IndividualSynset> indis = CollectionFactory.createSet();
            Set<OWLIndividual> individuals = getAllIndividuals(query.getKB());
            while (individuals.size() > 0) {
                OWLIndividual individual = individuals.iterator().next();
                individuals.remove(individual);
                if (individual.isAnonymous()) continue;
                if (reasoner.getDataPropertyValues(individual.asOWLNamedIndividual(),
                        query.getOWLProperty()).contains(query.getTargetValue())) {
                    try {
                        Node<OWLNamedIndividual> node = reasoner.getSameIndividuals(individual.asOWLNamedIndividual());
                        IndividualSynset synset = new IndividualSynsetImpl(node);
                        indis.add(synset);
                        individuals.removeAll(node.getEntities());
                    } catch (Exception e) {
                        warning = "Synonymous individuals are not considered";
                        Node<OWLNamedIndividual> node = new OWLNamedIndividualNode(individual.asOWLNamedIndividual());
                        IndividualSynset synset = new IndividualSynsetImpl(node);
                        indis.add(synset);
                        individuals.removeAll(node.getEntities());
                    }
                }
            }
            this.response = new SetOfIndividualSynsetsImpl(indis, warning);
        }
    }

    public void answer(GetDataPropertyTargets query) {
        if (query.getSourceIndividual().isAnonymous()) {
            this.response = new ErrorResponseImpl("Anonymous source individual is not supported");
            return;
        }
        try {
            OWLReasoner reasoner = getReasoner(query.getKB());
            java.util.Set<OWLLiteral> literals = reasoner.getDataPropertyValues(
                    query.getSourceIndividual().asOWLNamedIndividual(), query.getOWLProperty());
            if (!literals.isEmpty())
                this.response = new SetOfLiteralsImpl(literals, getWarning(reasoner));
        } catch (Exception e) {
            handle(e);
        }
    }

    public void answer(GetDescription query) {
        ProtocolVersion pVersion = new ProtocolVersionImpl(1, 0);
        Set<PublicKB> publicKBs = CollectionFactory.createSet();
        for (Map.Entry<IRI, String> entry : kbNameByIRI.entrySet()) {
            publicKBs.add(new PublicKBImpl(entry.getKey(), entry.getValue()));
        }
        Description description = new DescriptionImpl(this.factory.getReasonerName() + "-OWLlink",
                this.reasonerConfiguration.getConfigurations(), this.reasonerConfiguration.getReasonerVersion(),
                pVersion, Collections.<IRI>singleton(RetractRequest.EXTENSION_IRI), publicKBs);
        this.response = description;
    }

    public void answer(GetDisjointClasses query) {
        OWLReasoner reasoner = getReasoner(query.getKB());
        NodeSet<OWLClass> nodeSet = reasoner.getDisjointClasses(query.getObject());
        this.response = new ClassSynsetsImpl(nodeSet.getNodes(), getWarning(reasoner));
    }

    public void answer(GetDisjointDataProperties query) {
        OWLReasoner reasoner = getReasoner(query.getKB());
        NodeSet<OWLDataProperty> nodeSet = reasoner.getDisjointDataProperties(query.getOWLProperty());
        this.response = new DataPropertySynsetsImpl(nodeSet.getNodes(), getWarning(reasoner));
    }

    public void answer(GetDifferentIndividuals query) {
        if (query.getIndividual().isAnonymous()) {
            this.response = new ErrorResponseImpl("Anonymous individual is not supported");
            return;
        }
        OWLReasoner reasoner = getReasoner(query.getKB());
        NodeSet<OWLNamedIndividual> nodeSet = reasoner.getDifferentIndividuals(
                query.getIndividual().asOWLNamedIndividual());

        if (reasoner.getIndividualNodeSetPolicy() == IndividualNodeSetPolicy.BY_NAME) {
            nodeSet = computeSynonyms(nodeSet, reasoner);
        }

        this.response = new SetOfIndividualSynsetsImpl(convertTo(nodeSet), getWarning(reasoner));
    }


    protected Set<IndividualSynset> convertTo(NodeSet<OWLNamedIndividual> nodeSet) {
        Set<IndividualSynset> synonymsets = new HashSet<IndividualSynset>();

        for (Node<OWLNamedIndividual> node : nodeSet) {
            Set<OWLIndividual> indis = new HashSet<OWLIndividual>();
            for (OWLNamedIndividual indi : node.getEntities())
                indis.add(indi);
            synonymsets.add(new IndividualSynsetImpl(indis));
        }

        return synonymsets;
    }

    protected IndividualSynonyms convertTo(Node<OWLNamedIndividual> node) {
        Set<OWLIndividual> indi = new HashSet<OWLIndividual>();
        indi.addAll(node.getEntities());
        return new IndividualSynonymsImpl(indi);
    }

    public void answer(GetDisjointObjectProperties query) {
        OWLReasoner reasoner = getReasoner(query.getKB());
        NodeSet<OWLObjectPropertyExpression> nodeSet = reasoner.getDisjointObjectProperties(query.getOWLProperty());
        this.response = new ObjectPropertySynsetsImpl(convertToSynsets(nodeSet), getWarning(reasoner));
    }

    /**
     * Returns a set of nodes which only contains named nodes.
     *
     * @param nodeSet
     * @return
     */
    protected Set<Node<OWLObjectProperty>> convertToSynsets(NodeSet<OWLObjectPropertyExpression> nodeSet) {
        Set<Node<OWLObjectProperty>> set = new HashSet<Node<OWLObjectProperty>>();
        for (Node<OWLObjectPropertyExpression> node : nodeSet) {
            OWLlinkOWLObjectPropertyNode propNode = new OWLlinkOWLObjectPropertyNode();
            for (OWLObjectPropertyExpression expr : node) {
                if (!expr.isAnonymous())
                    propNode.add(expr.asOWLObjectProperty());
            }
            set.add(propNode);
        }
        return set;
    }

    protected Node<OWLObjectProperty> convertToNode(Node<OWLObjectPropertyExpression> nodeSet) {
        Set<OWLObjectProperty> set = new HashSet<OWLObjectProperty>();
        for (OWLObjectPropertyExpression node : nodeSet) {
            if (!node.isAnonymous())
                set.add(node.asOWLObjectProperty());
        }
        return new OWLlinkOWLObjectPropertyNode(set);
    }

    public void answer(org.semanticweb.owlapi.owllink.builtin.requests.GetEquivalentClasses query) {
        OWLReasoner reasoner = getReasoner(query.getKB());
        Node<OWLClass> node = reasoner.getEquivalentClasses(query.getObject());
        this.response = new SetOfClassesImpl(node.getEntities(), getWarning(reasoner));
    }

    public void answer(org.semanticweb.owlapi.owllink.builtin.requests.GetEquivalentDataProperties query) {
        try {
            OWLReasoner reasoner = getReasoner(query.getKB());
            this.response = new DataPropertySynonymsImpl(reasoner.getEquivalentDataProperties(
                    query.getObject()).getEntities(), getWarning(reasoner));
        } catch (Exception e) {
            handle(e);
        }
    }

    public void answer(GetSameIndividuals query) {
        if (query.getObject().isAnonymous())
            this.response = new ErrorResponseImpl("Anonymous Individuals are not supported");
        try {
            OWLReasoner reasoner = getReasoner(query.getKB());
            this.response = convertTo(reasoner.getSameIndividuals(query.getIndividual().asOWLNamedIndividual()));
        } catch (Exception e) {
            handle(e);
        }
    }

    public void answer(org.semanticweb.owlapi.owllink.builtin.requests.GetEquivalentObjectProperties query) {
        try {
            OWLReasoner reasoner = getReasoner(query.getKB());
            Collection<OWLObjectProperty> properties = new HashSet<OWLObjectProperty>();
            for (OWLObjectPropertyExpression expression : reasoner.getEquivalentObjectProperties(query.getObject())) {
                if (!expression.isAnonymous())
                    properties.add(expression.asOWLObjectProperty());
            }
            this.response = new SetOfObjectPropertiesImpl(properties, getWarning(reasoner));
        } catch (Exception e) {
            handle(e);
        }
    }

    public void answer(GetFlattenedDataPropertySources query) {
        if (query.isNegative())
            this.response = new ErrorResponseImpl("(negative=true) in GetDataPropertiesOfSource is not supported");
        else {
            try {
                OWLReasoner reasoner = getReasoner(query.getKB());
                Set<OWLIndividual> indis = CollectionFactory.createSet();
                for (OWLIndividual individual : getAllIndividuals(query.getKB())) {
                    if (individual.isAnonymous()) continue;
                    if (reasoner.getDataPropertyValues(individual.asOWLNamedIndividual(),
                            query.getOWLProperty()).contains(query.getTargetValue())) {
                        indis.add(individual);
                    }
                }
                this.response = new SetOfIndividualsImpl(indis, getWarning(reasoner));
            } catch (Exception e) {
                handle(e);
            }
        }
    }

    public void answer(GetFlattenedDifferentIndividuals query) {
        if (query.getIndividual().isAnonymous())
            this.response = new ErrorResponseImpl("Anonymous individiduals are not supported");
        else {
            OWLReasoner reasoner = getReasoner(query.getKB());
            NodeSet<OWLNamedIndividual> set = reasoner.getDifferentIndividuals(query.getIndividual().asOWLNamedIndividual());
            this.response = new SetOfIndividualsImpl(set, getWarning(reasoner));
        }
    }

    public void answer(GetFlattenedInstances query) {
        org.semanticweb.owlapi.owllink.builtin.requests.GetInstances subQuery = new
                org.semanticweb.owlapi.owllink.builtin.requests.GetInstances(query.getKB(),
                query.getClassExpression(), query.isDirect());
        answer(subQuery);
        SetOfIndividualSynsets response = (SetOfIndividualSynsets) this.response;
        this.response = new SetOfIndividualsImpl(response.getFlattened(), getWarning(getReasoner(query.getKB())));
    }

    public void answer(GetFlattenedObjectPropertySources query) {
        if (query.isNegative()) {
            this.response = new ErrorResponseImpl("(negative=true) is not supported");
            return;
        }
        if (query.getOWLIndividual().isAnonymous()) {
            this.response = new ErrorResponseImpl("Anonymous individual is not supported");
            return;
        }
        try {
            OWLReasoner reasoner = getReasoner(query.getKB());
            Set<OWLIndividual> setOfIndis = CollectionFactory.createSet();
            for (OWLIndividual indi : getAllIndividuals(query.getKB())) {
                NodeSet<OWLNamedIndividual> targets = reasoner.getObjectPropertyValues(indi.asOWLNamedIndividual(),
                        query.getOWLProperty());
                if (targets.containsEntity(query.getOWLIndividual().asOWLNamedIndividual())) {
                    setOfIndis.add(indi);
                }
            }
            this.response = new SetOfIndividualsImpl(setOfIndis, getWarning(reasoner));
        } catch (Exception e) {
            handle(e);
        }

    }

    public void answer(GetFlattenedObjectPropertyTargets query) {
        if (query.isNegative()) {
            this.response = new ErrorResponseImpl("(negative=true) is not supported");
            return;
        }
        if (query.getOWLIndividual().isAnonymous()) {
            this.response = new ErrorResponseImpl("Anonymous individual is not supported");
            return;
        }
        OWLReasoner reasoner = getReasoner(query.getKB());
        this.response = new SetOfIndividualsImpl(reasoner.getObjectPropertyValues(
                query.getOWLIndividual().asOWLNamedIndividual(), query.getOWLProperty()), getWarning(reasoner));
    }

    public void answer(GetFlattenedTypes query) {
        org.semanticweb.owlapi.owllink.builtin.requests.GetTypes subQuery =
                new org.semanticweb.owlapi.owllink.builtin.requests.GetTypes(query.getKB(), query.getIndividual(),
                        query.isDirect());
        answer(subQuery);
        ClassSynsets subResponse = (ClassSynsets) this.response;
        this.response = new ClassesImpl(subResponse.getFlattened(), subResponse.getWarning());
    }

    public void answer(org.semanticweb.owlapi.owllink.builtin.requests.GetInstances query) {
        OWLReasoner reasoner = getReasoner(query.getKB());
        NodeSet<OWLNamedIndividual> nodeSet = reasoner.getInstances(query.getClassExpression(), query.isDirect());
        if (reasoner.getIndividualNodeSetPolicy() == IndividualNodeSetPolicy.BY_NAME) {
            nodeSet = computeSynonyms(nodeSet, reasoner);
        }
        this.response = new SetOfIndividualSynsetsImpl(convertTo(nodeSet), getWarning(reasoner));
    }

    public void answer(GetKBLanguage query) {
        this.response = new ErrorResponseImpl("GetKBLanguage is not supported");

    }

    public void answer(GetObjectPropertiesBetween query) {
        if (query.isNegative()) {
            this.response = new ErrorResponseImpl("(negative=true) is not supported");
            return;
        }
        if (query.getSourceIndividual().isAnonymous()) {
            this.response = new ErrorResponseImpl("Anonymous source individuals are not supported");
            return;
        }
        if (query.getTargetIndividual().isAnonymous()) {
            this.response = new ErrorResponseImpl("Anonymous target individuals are not supported");
            return;
        }
        OWLReasoner reasoner = getReasoner(query.getKB());
        java.util.Set<OWLObjectProperty> properties = CollectionFactory.createSet();
        java.util.Set<Node<OWLObjectProperty>> synsetSet = CollectionFactory.createSet();
        boolean topConsidered = false;
        for (OWLObjectProperty property : getAllObjectProperties(query.getKB())) {
            if (properties.contains(property)) continue;
            NodeSet<OWLNamedIndividual> literals =
                    reasoner.getObjectPropertyValues(query.getSourceIndividual().asOWLNamedIndividual(), property);
            if (literals.containsEntity(query.getTargetIndividual().asOWLNamedIndividual())) {
                properties.add(property);
                Node<OWLObjectProperty> node = convertToNode(reasoner.getEquivalentObjectProperties(property));
                if (!topConsidered) {
                    topConsidered = node.contains(this.topObjectProperty);
                }
                synsetSet.add(node);
            }
        }
        if (!topConsidered) {
            OWLlinkOWLObjectPropertyNode node = new OWLlinkOWLObjectPropertyNode(this.topObjectProperty);
            synsetSet.add(node);
            this.response = new SetOfObjectPropertySynsetsImpl(synsetSet, "Equivalents to TOP not considered");
        } else
            this.response = new SetOfObjectPropertySynsetsImpl(synsetSet, getWarning(reasoner));
    }

    private Set<Node<OWLObjectProperty>> getAllEquivalentObjectProperties(IRI kb) {
        OWLReasoner reasoner = getReasoner(kb);
        Set<Node<OWLObjectProperty>> setOfNodes = CollectionFactory.createSet();
        Set<OWLObjectProperty> properties = getAllObjectProperties(kb);
        while (!properties.isEmpty()) {
            OWLObjectProperty property = properties.iterator().next();
            properties.remove(property);
            Node<OWLObjectProperty> node = convertToNode(reasoner.getEquivalentObjectProperties(property));
            setOfNodes.add(node);
            properties.removeAll(node.getEntities());
        }
        return setOfNodes;
    }

    public void answer(GetObjectPropertiesOfSource query) {
        if (query.isNegative()) {
            this.response = new ErrorResponseImpl("(negative=true) is not supported");
            return;
        }
        OWLReasoner reasoner = getReasoner(query.getKB());
        if (query.getIndividual().isAnonymous()) {
            this.response = new ErrorResponseImpl("Anonymous individual is not supported");
            return;
        }
        Set<Node<OWLObjectProperty>> synsets = CollectionFactory.createSet();
        Set<Node<OWLObjectProperty>> setOfNodes = getAllEquivalentObjectProperties(query.getKB());
        boolean topConsidered = false;

        final OWLNamedIndividual individual = query.getIndividual().asOWLNamedIndividual();
        for (Node<OWLObjectProperty> propertyNode : setOfNodes) {
            NodeSet<OWLNamedIndividual> nodeSet = reasoner.getObjectPropertyValues(individual, propertyNode.getRepresentativeElement());
            if (!nodeSet.isEmpty()) {
                synsets.add(propertyNode);
                if (!topConsidered) {
                    topConsidered = propertyNode.contains(this.topObjectProperty);
                }
            }
        }
        if (!topConsidered) {
            synsets.add(convertToNode(reasoner.getEquivalentObjectProperties(this.topObjectProperty)));
        }
        this.response = new SetOfObjectPropertySynsetsImpl(synsets, getWarning(reasoner));
    }

    public void answer(GetObjectPropertiesOfTarget query) {
        if (query.isNegative()) {
            this.response = new ErrorResponseImpl("(negative=true) is not supported");
            return;
        }
        if (query.getIndividual().isAnonymous()) {
            this.response = new ErrorResponseImpl("Anonymous individual is not supported");
            return;
        }
        final OWLReasoner reasoner = getReasoner(query.getKB());
        Set<Node<OWLObjectProperty>> found = CollectionFactory.createSet();
        Set<Node<OWLObjectProperty>> props = getAllEquivalentObjectProperties(query.getKB());
        boolean topConsidered = false;

        for (OWLIndividual indi : getAllIndividuals(query.getKB())) {
            if (indi.isAnonymous()) continue;
            for (Node<OWLObjectProperty> prop : props) {
                NodeSet<OWLNamedIndividual> nodeSet = reasoner.getObjectPropertyValues(indi.asOWLNamedIndividual(),
                        prop.getRepresentativeElement());
                if (nodeSet.containsEntity(query.getIndividual().asOWLNamedIndividual())) {
                    found.add(prop);
                    if (!topConsidered) {
                        topConsidered = prop.contains(this.topObjectProperty);
                    }
                }
            }
        }
        if (!topConsidered) {
            found.add(convertToNode(reasoner.getEquivalentObjectProperties(this.topObjectProperty)));
        }
        this.response = new SetOfObjectPropertySynsetsImpl(found, getWarning(reasoner));
    }

    public void answer(GetObjectPropertySources query) {
        if (query.isNegative()) {
            this.response = new ErrorResponseImpl("(negative=true) is not supported");
            return;
        }
        if (query.getOWLIndividual().isAnonymous()) {
            this.response = new ErrorResponseImpl("Anonymous individual is not supported");
            return;
        }
        try {
            final OWLReasoner reasoner = getReasoner(query.getKB());
            final Set<IndividualSynset> synsets = CollectionFactory.createSet();
            for (OWLIndividual indi : getAllIndividuals(query.getKB())) {
                NodeSet<OWLNamedIndividual> targets = reasoner.getObjectPropertyValues(indi.asOWLNamedIndividual(),
                        query.getOWLProperty());
                if (targets.containsEntity(query.getOWLIndividual().asOWLNamedIndividual())) {
                    synsets.add(computeSynonyms(indi, reasoner));
                }
            }
            this.response = new SetOfIndividualSynsetsImpl(synsets, getWarning(reasoner));
        } catch (Exception e) {
            handle(e);
        }
    }


    public void answer(GetObjectPropertyTargets query) {
        if (query.isNegative()) {
            this.response = new ErrorResponseImpl("(negative=true) is not supported");
            return;
        }
        if (query.getOWLIndividual().isAnonymous()) {
            this.response = new ErrorResponseImpl("Anonymous individual is not supported");
            return;
        }
        OWLReasoner reasoner = getReasoner(query.getKB());
        NodeSet<OWLNamedIndividual> nodeSet = reasoner.getObjectPropertyValues(
                query.getOWLIndividual().asOWLNamedIndividual(), query.getOWLProperty());
        if (reasoner.getIndividualNodeSetPolicy() == IndividualNodeSetPolicy.BY_NAME) {
            nodeSet = computeSynonyms(nodeSet, reasoner);
        }
        this.response = new SetOfIndividualSynsetsImpl(convertToIndividualSynsetSet(nodeSet), getWarning(reasoner));
    }

    protected Set<IndividualSynset> convertToIndividualSynsetSet(NodeSet<OWLNamedIndividual> nodeSet) {
        Set<IndividualSynset> synsets = CollectionFactory.createSet();
        for (Node<OWLNamedIndividual> node : nodeSet) {
            IndividualSynset synset = new IndividualSynsetImpl(node);
            synsets.add(synset);
        }
        return synsets;
    }

    protected IndividualSynset computeSynonyms(OWLIndividual individual, OWLReasoner reasoner) {
        try {
            Node<OWLNamedIndividual> node = reasoner.getSameIndividuals(individual.asOWLNamedIndividual());
            return new IndividualSynsetImpl(node);
        } catch (Exception e) {
            logWarning(reasoner, "Synonyms could not be considered " + e.toString());
        }
        return new IndividualSynsetImpl(CollectionFactory.createSet(individual));
    }


    protected NodeSet<OWLNamedIndividual> computeSynonyms(NodeSet<OWLNamedIndividual> nodeSet,
                                                          OWLReasoner reasoner) {
        try {
            Set<Node<OWLNamedIndividual>> convertedSet = new HashSet<Node<OWLNamedIndividual>>();
            Set<OWLNamedIndividual> individuals = new HashSet<OWLNamedIndividual>();
            individuals.addAll(nodeSet.getFlattened());
            while (individuals.size() > 0) {
                OWLNamedIndividual indi = individuals.iterator().next();
                individuals.remove(indi);
                Node<OWLNamedIndividual> node = reasoner.getSameIndividuals(indi);
                individuals.removeAll(node.getEntities());
                convertedSet.add(node);
            }
            return new OWLNamedIndividualNodeSet(convertedSet);
        } catch (Exception e) {
            logWarning(reasoner, "Synonyms could not be considered " + e.toString());
        }
        return nodeSet;
    }

    public void answer(GetPrefixes query) {
        PrefixManager manager = prov.getPrefixes(query.getKB());
        Map<String, String> prefixes = manager.getPrefixName2PrefixMap();
        this.response = new PrefixesImpl(prefixes);
    }

    public void answer(GetSettings query) {
        AbstractOWLlinkReasonerConfiguration config = this.configurationsByKB.get(query.getKB());
        if (config == null) {
            this.response = new SettingsImpl(reasonerConfiguration.getSettings());
        } else {
            this.response = new SettingsImpl(config.getSettings());
        }
    }

    public void answer(org.semanticweb.owlapi.owllink.builtin.requests.GetSubClasses query) {
        try {
            OWLReasoner reasoner = getReasoner(query.getKB());
            NodeSet<OWLClass> nodeSet = reasoner.getSubClasses(query.getClassExpression(), query.isDirect());
            if (!nodeSet.isEmpty())
                this.response = new SetOfClassSynsetsImpl(nodeSet.getNodes());
            else if (!query.getClassExpression().isOWLNothing()) {
                nodeSet = new OWLClassNodeSet(getOntologyManager(query.getKB()).getOWLDataFactory().getOWLNothing());
                this.response = new SetOfClassSynsetsImpl(nodeSet.getNodes(), getWarning(reasoner));
            } else {
                this.response = new SetOfClassSynsetsImpl(Collections.<Node<OWLClass>>emptySet(), getWarning(reasoner));
            }
        } catch (Exception e) {
            handle(e);
        }
    }

    public void answer(GetSubClassHierarchy query) {
        OWLClass superClass;
        if (query.getOWLClass() == null) {
            superClass = getOntologyManager(query.getKB()).getOWLDataFactory().getOWLThing();
        } else {
            superClass = query.getOWLClass();
        }
        OWLReasoner reasoner = getReasoner(query.getKB());
        Set<HierarchyPair<OWLClass>> pairs = CollectionFactory.createSet();
        OWLClassNode equivalentClasses = new OWLClassNode();
        equivalentClasses.add(superClass);
        for (OWLClass clazz : reasoner.getEquivalentClasses(superClass).getEntities())
            equivalentClasses.add(clazz);
        Node<OWLClass> superClassSynset = equivalentClasses;
        List<Node<OWLClass>> nextSynsets = new Vector<Node<OWLClass>>();

        if (superClassSynset.getSize() > 0) {
            nextSynsets.add(superClassSynset);
        }
        final OWLClass nothing = getOntologyManager(query.getKB()).getOWLDataFactory().getOWLNothing();
        while (!nextSynsets.isEmpty()) {
            Node<OWLClass> synset = nextSynsets.remove(0);
            if (synset.getSize() > 0) {
                NodeSet<OWLClass> subClasses = reasoner.getSubClasses(synset.getRepresentativeElement(), true);
                OWLClassNodeSet ruledOutSubClasses = new OWLClassNodeSet();
                //rule out nothing set!
                for (Node<OWLClass> classes : subClasses.getNodes()) {
                    if (!classes.contains(nothing)) {
                        ruledOutSubClasses.addNode(classes);
                    }
                }
                subClasses = ruledOutSubClasses;
                Set<Node<OWLClass>> setOfSynsets = CollectionFactory.createSet();
                for (Node<OWLClass> classes : subClasses) {
                    setOfSynsets.add(classes);
                }
                if (setOfSynsets.size() > 0) {
                    SubEntitySynsets<OWLClass> subClassSynset = new SubClassSynsets(setOfSynsets);
                    pairs.add(new HierarchyPairImpl<OWLClass>(synset, subClassSynset));
                    nextSynsets.addAll(setOfSynsets);
                }
            }
        }

        Node<OWLClass> unsatisfiableClasses = reasoner.getUnsatisfiableClasses();
        this.response = new ClassHierarchyImpl(pairs, unsatisfiableClasses, getWarning(reasoner));
    }

    public void answer(org.semanticweb.owlapi.owllink.builtin.requests.GetSubDataProperties query) {
        java.util.Set<java.util.Set<OWLDataProperty>> result;
        final OWLReasoner reasoner = getReasoner(query.getKB());
        NodeSet<OWLDataProperty> nodeSet = reasoner.getSubDataProperties(query.getProperty(), query.isDirect());
        this.response = new SetOfDataPropertySynsetsImpl(nodeSet.getNodes(), getWarning(reasoner));
    }

    public void answer(GetSubDataPropertyHierarchy query) {
        OWLDataProperty superClass;
        if (query.getOWLProperty() == null) {
            superClass = getOntologyManager(query.getKB()).getOWLDataFactory().getOWLTopDataProperty();
        } else {
            superClass = query.getOWLProperty();
        }
        OWLReasoner reasoner = getReasoner(query.getKB());
        Set<HierarchyPair<OWLDataProperty>> pairs = CollectionFactory.createSet();
        final OWLDataProperty nothing = getOntologyManager(query.getKB()).getOWLDataFactory().getOWLBottomDataProperty();
        List<Node<OWLDataProperty>> nextSynsets = new Vector<Node<OWLDataProperty>>();
        if (superClass.equals(getOntologyManager(query.getKB()).getOWLDataFactory().getOWLTopDataProperty()) && !reasonerKnowsTopProperty) {
            //not every reasoner knowns about the top property:
            Set<OWLDataProperty> rootProperties = CollectionFactory.createSet();
            Map<OWLDataProperty, NodeSet<OWLDataProperty>> subPropertiesByProperty = CollectionFactory.createMap();
            rootProperties.addAll(getAllDataProperties(query.getKB()));
            Set<OWLDataProperty> allProperties = CollectionFactory.createSet();
            allProperties.addAll(rootProperties);

            for (OWLDataProperty property : allProperties) {
                if (property.isOWLTopDataProperty()) continue;
                NodeSet<OWLDataProperty> subProperties = reasoner.getSubDataProperties(property, true);
                boolean containsNothing = false;
                for (Node<OWLDataProperty> props : subProperties) {
                    rootProperties.removeAll(props.getEntities());
                    containsNothing = props.contains(nothing);
                }
                if (!containsNothing) {
                    subPropertiesByProperty.put(property, subProperties);
                }
            }
            Set<Node<OWLDataProperty>> setOfSynsets = CollectionFactory.createSet();
            for (OWLDataProperty prop : rootProperties) {
                Node<OWLDataProperty> equis = reasoner.getEquivalentDataProperties(prop);
                setOfSynsets.add(equis);
            }
            OWLDataPropertyNode rootSynset = new OWLDataPropertyNode(getOntologyManager(query.getKB()).getOWLDataFactory().getOWLTopDataProperty());
            if (!setOfSynsets.isEmpty()) {
                SubDataPropertySynsets synsets = new SubDataPropertySynsets(setOfSynsets);
                pairs.add(new HierarchyPairImpl<OWLDataProperty>(rootSynset, synsets));
                nextSynsets.addAll(setOfSynsets);
            }
        } else {
            nextSynsets.add(reasoner.getEquivalentDataProperties(superClass));
        }
        while (!nextSynsets.isEmpty()) {
            Node<OWLDataProperty> synset = nextSynsets.remove(0);
            if (synset.getSize() > 0) {
                NodeSet<OWLDataProperty> subClasses = reasoner.getSubDataProperties(synset.getRepresentativeElement(), true);
                OWLDataPropertyNodeSet ruledOut = new OWLDataPropertyNodeSet();
                for (Node<OWLDataProperty> prop : subClasses) {
                    if (!prop.contains(nothing))
                        ruledOut.addNode(prop);
                }
                subClasses = ruledOut;
                if (!subClasses.isEmpty()) {
                    SubEntitySynsets<OWLDataProperty> subSynsets = new SubDataPropertySynsets(subClasses.getNodes());
                    pairs.add(new HierarchyPairImpl<OWLDataProperty>(synset, subSynsets));
                    nextSynsets.addAll(subClasses.getNodes());
                }
            }
        }
        Node<OWLDataProperty> unsatisfiables = reasoner.getEquivalentDataProperties(bottomDataProperty);
        if (!reasonerKnowsTopProperty) {
            String warning = "Equivalent top properties and unsatisfiable properties are not considered";
            this.response = new DataPropertyHierarchyImpl(pairs, unsatisfiables, warning);
        } else
            this.response = new DataPropertyHierarchyImpl(pairs, unsatisfiables, getWarning(reasoner));


    }

    public void answer(org.semanticweb.owlapi.owllink.builtin.requests.GetSubObjectProperties query) {
        final OWLReasoner reasoner = getReasoner(query.getKB());
        Set<Node<OWLObjectProperty>> nodeSet = convertToSynsets(reasoner.getSubObjectProperties(query.getOWLObjectPropertyExpression(), query.isDirect()));
        this.response = new SetOfObjectPropertySynsetsImpl(nodeSet, getWarning(reasoner));
    }

    boolean reasonerKnowsTopProperty = true;

    public void setReasonerKnowsTopProperty(boolean b) {
        this.reasonerKnowsTopProperty = b;
    }

    public void answer(GetSubObjectPropertyHierarchy query) {
        OWLObjectProperty superProperty;
        if (query.getObjectProperty() == null) {
            superProperty = getOntologyManager(query.getKB()).getOWLDataFactory().getOWLTopObjectProperty();
        } else {
            superProperty = query.getObjectProperty();
        }
        OWLReasoner reasoner = getReasoner(query.getKB());
        Set<HierarchyPair<OWLObjectProperty>> pairs = CollectionFactory.createSet();
        final OWLObjectProperty nothing = getOntologyManager(query.getKB()).getOWLDataFactory().getOWLBottomObjectProperty();
        List<Node<OWLObjectProperty>> nextSynsets = new Vector<Node<OWLObjectProperty>>();
        if (superProperty.equals(getOntologyManager(query.getKB()).getOWLDataFactory().getOWLTopObjectProperty()) && !reasonerKnowsTopProperty) {
            //not every reasoner knowns about the top property:
            Set<OWLObjectProperty> rootProperties = CollectionFactory.createSet();
            Map<OWLObjectProperty, NodeSet<OWLObjectPropertyExpression>> subPropertiesByProperty = CollectionFactory.createMap();
            rootProperties.addAll(getAllObjectProperties(query.getKB()));
            Set<OWLObjectProperty> allProperties = CollectionFactory.createSet();
            allProperties.addAll(rootProperties);

            for (OWLObjectProperty property : allProperties) {
                if (property.isOWLTopObjectProperty()) continue;
                NodeSet<OWLObjectPropertyExpression> subProperties = reasoner.getSubObjectProperties(property, true);
                boolean containsNothing = false;
                for (Node<OWLObjectPropertyExpression> props : subProperties) {
                    rootProperties.removeAll(props.getEntities());
                    containsNothing = props.contains(nothing);
                }
                if (!containsNothing) {
                    subPropertiesByProperty.put(property, subProperties);
                }
            }
            Set<Node<OWLObjectProperty>> setOfSynsets = CollectionFactory.createSet();
            for (OWLObjectProperty prop : rootProperties) {
                Node<OWLObjectProperty> equis = convertToNode(reasoner.getEquivalentObjectProperties(prop));
                if (!equis.getEntities().isEmpty())
                    setOfSynsets.add(equis);
            }
            Node<OWLObjectProperty> rootSynset = new OWLlinkOWLObjectPropertyNode(getOntologyManager(query.getKB()).getOWLDataFactory().getOWLTopObjectProperty());
            if (!setOfSynsets.isEmpty()) {
                pairs.add(new HierarchyPairImpl<OWLObjectProperty>(rootSynset, new
                        SubObjectPropertySynsets(setOfSynsets)));
                nextSynsets.addAll(setOfSynsets);
            }
        } else {
            Node<OWLObjectProperty> equivalentObjectProps = convertToNode(reasoner.getEquivalentObjectProperties(superProperty));
            if (!equivalentObjectProps.getEntities().isEmpty())
                nextSynsets.add(equivalentObjectProps);
        }
        while (!nextSynsets.isEmpty()) {
            Node<OWLObjectProperty> synset = nextSynsets.remove(0);
            if (synset.getSize() > 0) {
                NodeSet<OWLObjectPropertyExpression> subPropertyExpression = reasoner.getSubObjectProperties(synset.getRepresentativeElement(), true);
                OWLObjectPropertyNodeSet ruledOutSubClasses = new OWLObjectPropertyNodeSet();
                for (Node<OWLObjectPropertyExpression> properties : subPropertyExpression) {
                    if (!properties.contains(nothing)) {
                        ruledOutSubClasses.addNode(properties);
                    }
                }
                subPropertyExpression = ruledOutSubClasses;
                if (!subPropertyExpression.isEmpty()) {
                    Set<Node<OWLObjectProperty>> setOfSynsets = CollectionFactory.createSet();
                    for (Node<OWLObjectPropertyExpression> classes : subPropertyExpression) {
                        OWLlinkOWLObjectPropertyNode node = new OWLlinkOWLObjectPropertyNode();
                        for (OWLObjectPropertyExpression expr : classes) {
                            if (expr.isAnonymous()) continue;
                            node.add(expr.asOWLObjectProperty());
                        }
                        setOfSynsets.add(node);
                    }
                    SubEntitySynsets<OWLObjectProperty> subClassSynset = new SubObjectPropertySynsets(setOfSynsets);
                    pairs.add(new HierarchyPairImpl<OWLObjectProperty>(synset, subClassSynset));
                    nextSynsets.addAll(setOfSynsets);
                }
                /* NodeSet<OWLObjectProperty> subClasses = reasoner.getSubObjectProperties(synset.getRepresentativeElement(), true);
             OWLObjectPropertyNodeSet ruledOutSubClasses = new OWLObjectPropertyNodeSet();
             for (Node<OWLObjectProperty> classes : subClasses) {
                 if (!classes.contains(nothing))
                     ruledOutSubClasses.addNode(classes);
             }
             subClasses = ruledOutSubClasses;
             if (!subClasses.isEmpty()) {
                 Set<Node<OWLObjectProperty>> setOfSynsets = CollectionFactory.createSet();
                 for (Node<OWLObjectProperty> classes : subClasses) {
                     setOfSynsets.add(classes);
                 }
                 SubEntitySynsets<OWLObjectProperty> subClassSynset = new SubObjectPropertySynsets(setOfSynsets);
                 pairs.add(new HierarchyPairImpl<OWLObjectProperty>(synset, subClassSynset));
                 nextSynsets.addAll(setOfSynsets);
             }   */
            }
        }
        Set<OWLObjectProperty> unsatisfiableClasses = CollectionFactory.createSet();//reasoner.getUnsatisfiableClasses();
        Node<OWLObjectProperty> unsatisfiableSynset;
        if (unsatisfiableClasses.contains(getOntologyManager(query.getKB()).getOWLDataFactory().getOWLBottomObjectProperty()))
            unsatisfiableSynset = new OWLlinkOWLObjectPropertyNode(unsatisfiableClasses);
        else {
            Set<OWLObjectProperty> newSet = new HashSet<OWLObjectProperty>();
            newSet.addAll(unsatisfiableClasses);
            newSet.add(getOntologyManager(query.getKB()).getOWLDataFactory().getOWLBottomObjectProperty());
            unsatisfiableSynset = new OWLlinkOWLObjectPropertyNode(newSet);
        }
        if (!reasonerKnowsTopProperty) {
            String warning = "Equivalent top properties and unsatisfiable properties are not considered";
            this.response = new ObjectPropertyHierarchyImpl(pairs, unsatisfiableSynset, warning);
        } else
            this.response = new ObjectPropertyHierarchyImpl(pairs, unsatisfiableSynset, getWarning(reasoner));
    }

    protected Response getResponse() {
        return this.response;
    }

    public void answer(org.semanticweb.owlapi.owllink.builtin.requests.GetSuperClasses query) {
        try {
            OWLReasoner reasoner = getReasoner(query.getKB());
            NodeSet<OWLClass> nodeSet = reasoner.getSuperClasses(query.getOWLClassExpression(), query.isDirect());
            this.response = new SetOfClassSynsetsImpl(nodeSet.getNodes(), getWarning(reasoner));
        } catch (Exception e) {
            handle(e);
        }
    }

    public void answer(Request query) {
        if (query instanceof RetractRequest) {
            RetractRequest request = (RetractRequest) query;
            OWLReasoner reasoner = getReasoner(request.getKB());
            if (reasoner != null) {
                OWLOntology ontology = getOntologyManager(request.getKB()).getOntology(request.getKB());
                getOntologyManager(request.getKB()).removeAxioms(ontology, request.getAxioms());
                this.response = new OKImpl();
                //getReasoner(request.getKB()).prepareReasoner();
            }
        } else
            this.response = new ErrorResponseImpl(query.getClass().getSimpleName() + " is not supported");
    }

    public void answer(org.semanticweb.owlapi.owllink.builtin.requests.GetSuperDataProperties query) {
        OWLReasoner reasoner = getReasoner(query.getKB());
        NodeSet<OWLDataProperty> nodeSet = reasoner.getSuperDataProperties(query.getProperty(), query.isDirect());
        this.response = new SetOfDataPropertySynsetsImpl(nodeSet.getNodes(), getWarning(reasoner));
    }

    public void answer(org.semanticweb.owlapi.owllink.builtin.requests.GetSuperObjectProperties query) {
        OWLReasoner reasoner = getReasoner(query.getKB());
        this.response = new SetOfObjectPropertySynsetsImpl(convertToSynsets(reasoner.getSuperObjectProperties(query.getProperty(), query.isDirect())), getWarning(reasoner));
    }


    public void answer(org.semanticweb.owlapi.owllink.builtin.requests.GetTypes query) {
        OWLIndividual individual = query.getIndividual();
        if (individual.isAnonymous()) {
            this.response = new ErrorResponseImpl("Anonymous individual is not supported");
            return;
        }
        OWLReasoner reasoner = getReasoner(query.getKB());
        this.response = new ClassSynsetsImpl(reasoner.getTypes(query.getIndividual().asOWLNamedIndividual(), query.isDirect()).getNodes(), getWarning(reasoner));
    }

    public void answer(IsClassSatisfiable query) {
        OWLReasoner reasoner = getReasoner(query.getKB());

        try {
            boolean isSatisfiable = reasoner.isSatisfiable(query.getObject());
            BooleanResponse response = new BooleanResponseImpl(isSatisfiable);
            this.response = response;
        } catch (Exception e) {
            handle(e);
            e.printStackTrace();
        }
    }

    public void answer(IsDataPropertySatisfiable query) {
        this.response = new ErrorResponseImpl("IsDataPropertySatisfiable is not supported");

    }


    public void answer(IsKBConsistentlyDeclared query) {
        this.response = new ErrorResponseImpl("IsKBConsistentlyDeclared is not supported");

    }

    public void answer(IsKBSatisfiable query) {
        this.response = new ErrorResponseImpl("IsKBSatisfiable is not supported");
    }

    public void answer(IsEntailed query) {
        OWLReasoner reasoner = getReasoner(query.getKB());
        this.response = new BooleanResponseImpl(reasoner.isEntailed(query.getAxiom()), getWarning(reasoner));
    }

    public void answer(IsEntailedDirect query) {
        OWLReasoner reasoner = getReasoner(query.getKB());
        if (query.isOWLClassAssertionAxiom()) {
            OWLClassAssertionAxiom axiom = query.asOWLClassAssertionAxiom();
            if (axiom.getIndividual().isAnonymous())
                this.response = new ErrorResponseImpl("Anonymous individuals are not supported");
            else {
                this.response = new BooleanResponseImpl(reasoner.getInstances(axiom.getClassExpression(), true).containsEntity(axiom.getIndividual().asOWLNamedIndividual()));
            }
        } else if (query.isOWLSubClassOfAxiom()) {
            OWLSubClassOfAxiom axiom = query.asOWLSubClassOfAxiom();
            if (axiom.getSubClass().isAnonymous() && axiom.getSuperClass().isAnonymous()) {
                this.response = new ErrorResponseImpl("Both sub- and superclass are anonymous. This is not supported");
            } else if (axiom.getSubClass().isAnonymous()) {
                this.response = new BooleanResponseImpl(reasoner.getSuperClasses(axiom.getSubClass(), true).containsEntity(axiom.getSuperClass().asOWLClass()), getWarning(reasoner));
            } else if (axiom.getSuperClass().isAnonymous()) {
                this.response = new BooleanResponseImpl((reasoner.getSubClasses(axiom.getSuperClass(), true)).containsEntity(axiom.getSubClass().asOWLClass()), getWarning(reasoner));
            } else {
                this.response = new BooleanResponseImpl(reasoner.getSubClasses(axiom.getSuperClass().asOWLClass(), true).containsEntity(axiom.getSubClass().asOWLClass()), getWarning(reasoner));
            }
        } else if (query.isOWLSubDataPropertyOfAxiom()) {
            OWLSubDataPropertyOfAxiom axiom = query.asOWLSubDataPropertOfAxiom();
            if (axiom.getSubProperty().isAnonymous() && axiom.getSuperProperty().isAnonymous()) {
                this.response = new ErrorResponseImpl("Both sub- and superclass are anonymous. This is not supported");
            } else if (axiom.getSubProperty().isAnonymous()) {
                this.response = new BooleanResponseImpl(reasoner.getSuperDataProperties(axiom.getSubProperty().asOWLDataProperty(), true).containsEntity(axiom.getSuperProperty().asOWLDataProperty()), getWarning(reasoner));
            } else if (axiom.getSuperProperty().isAnonymous()) {
                this.response = new BooleanResponseImpl((reasoner.getSubDataProperties(axiom.getSuperProperty().asOWLDataProperty(), true)).containsEntity(axiom.getSubProperty().asOWLDataProperty()), getWarning(reasoner));
            } else {
                this.response = new BooleanResponseImpl(reasoner.getSubDataProperties(axiom.getSuperProperty().asOWLDataProperty(), true).containsEntity(axiom.getSubProperty().asOWLDataProperty()), getWarning(reasoner));
            }
        } else if (query.isOWLSubObjectPropertyOfAxiom()) {
            OWLSubObjectPropertyOfAxiom axiom = query.asOWLSubObjectPropertOfAxiom();
            if (axiom.getSubProperty().isAnonymous() && axiom.getSuperProperty().isAnonymous()) {
                this.response = new ErrorResponseImpl("Both sub- and superclass are anonymous. This is not supported");
            } else if (axiom.getSubProperty().isAnonymous()) {
                this.response = new BooleanResponseImpl(reasoner.getSuperObjectProperties(axiom.getSubProperty(), true).containsEntity(axiom.getSuperProperty().asOWLObjectProperty()), getWarning(reasoner));
            } else if (axiom.getSuperProperty().isAnonymous()) {
                this.response = new BooleanResponseImpl((reasoner.getSubObjectProperties(axiom.getSuperProperty(), true)).containsEntity(axiom.getSubProperty().asOWLObjectProperty()), getWarning(reasoner));
            } else {
                this.response = new BooleanResponseImpl(reasoner.getSubObjectProperties(axiom.getSuperProperty().asOWLObjectProperty(), true).containsEntity(axiom.getSubProperty().asOWLObjectProperty()), getWarning(reasoner));
            }
        }
    }

    public void answer(IsObjectPropertySatisfiable query) {
        this.response = new ErrorResponseImpl("IsObjectPropertySatisfiable is not supported");

    }

    public synchronized void answer(LoadOntologies query) {
        OWLlinkIRIMapper mapper = new OWLlinkIRIMapper(query.getIRIMapping());
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        manager.addIRIMapper(mapper);
        OWLReasoner reasoner = getReasoner(query.getKB());
        try {
            for (IRI iri : query.getOntologyIRIs()) {
                manager.loadOntology(iri);
            }
            manager.removeIRIMapper(mapper);
            for (OWLOntology ontology : manager.getOntologies()) {
                this.getOntologyManager(query.getKB()).addAxioms(this.getOntologyManager(query.getKB()).getOntology(query.getKB()), ontology.getAxioms());
            }
            reloadReasoner(query.getKB());
            //reasoner.flush();
            //reasoner.prepareReasoner();
            this.response = new OKImpl();
        } catch (Exception e) {
            handle(e);
        }
    }

    protected void reloadReasoner(IRI kb){
        OWLReasoner reasoner = getReasoner(kb);
        reasoner.flush();
    }

    class OWLlinkIRIMapper implements OWLOntologyIRIMapper {
        private List<IRIMapping> mappings;

        public OWLlinkIRIMapper(List<IRIMapping> mappings) {
            this.mappings = mappings;
        }

        public IRI getDocumentIRI(IRI iri) {
            for (IRIMapping mapping : mappings) {
                if (iri.toString().startsWith(mapping.key)) {
                    String result = mapping.value.toString();
                    result += iri.toString().substring(mapping.key.length());
                    try {
                        return IRI.create(result);
                    } catch (Exception e) {
                    }
                }
            }
            return iri;
        }
    }


    public void answer(ReleaseKB query) {
        OWLReasoner reasoner = getReasoner(query.getKB());
        getOntologyManager(query.getKB()).removeOntology(getOntologyManager(query.getKB()).getOntology(query.getKB()));
        this.reasonersByKB.remove(query.getKB());
        this.kbNameByIRI.remove(query.getKB());
        this.configurationsByKB.remove(query.getKB());
        this.warningsByReasoners.remove(query.getKB());
        try {
            reasoner.dispose();
            //  if (reasoner instanceof OWLOntologyChangeListener)
            //    manager.removeOntologyChangeListener((OWLOntologyChangeListener) reasoner);
        } catch (Exception e) {
        }
        this.response = new OKImpl();
    }


    public void answer(org.semanticweb.owlapi.owllink.builtin.requests.Set query) {
        AbstractOWLlinkReasonerConfiguration config = this.configurationsByKB.get(query.getKB());
        if (config == null) {
            config = new AbstractOWLlinkReasonerConfiguration(this.reasonerConfiguration.getOWLReasonerConfiguration());
            config.add(this.reasonerConfiguration);
            this.configurationsByKB.put(query.getKB(), config);
        }
        if (!config.set(query.getKey(), query.getValue())) {
            this.response = new ErrorResponseImpl("The given set is not supported");
        } else {
            this.response = new OKImpl();
        }
        if (query.getKey().equals(AbstractOWLlinkReasonerConfiguration.ABBREVIATES_IRIS)) {
            //set appreviated IRIs!
            OWLlinkLiteral literal = query.getValue().iterator().next();
            boolean abbrevIRIs = Boolean.valueOf(literal.getValue());
            prov.setBlocked(query.getKB(), !abbrevIRIs);
        }
    }

    public void answer(Tell request) {
        try {
            OWLOntology ontology = getOntologyManager(request.getKB()).getOntology(request.getKB());
            getOntologyManager(request.getKB()).addAxioms(ontology, request.getAxioms());
            reloadReasoner(request.getKB());
            this.response = new OKImpl();

            // getReasoner(request.getKB()).prepareReasoner();
        } catch (Exception e) {
            handle(e);
        }

    }


    public final static class PairsIterator<O> implements Iterator<PairsIterator.Pair<O>>, Iterable<PairsIterator.Pair<O>> {
        List<O> elements;
        int innerIndex = 0;
        int outerIndex = 0;
        Pair<O> current;
        boolean hasCurrent = false;

        public PairsIterator(java.util.Set<O> elements) {
            this.elements = new Vector<O>(elements);
            this.innerIndex = 1;
            this.outerIndex = 0;
            if (elements.size() == 1) {
                current = new Pair<O>();
                current.first = this.elements.get(0);
                current.second = null;
                hasCurrent = true;
            }
            if (elements.size() > 1) {
                current = new Pair<O>();
                current.first = this.elements.get(0);
                current.second = this.elements.get(1);
                hasCurrent = true;
            }
        }

        public boolean hasNext() {
            while (!hasCurrent && outerIndex < this.elements.size()) {
                if (innerIndex < this.elements.size() - 1) {
                    innerIndex++;
                    current = new Pair<O>();
                    current.first = this.elements.get(outerIndex);
                    current.second = this.elements.get(innerIndex);
                    hasCurrent = true;
                } else {
                    outerIndex++;
                    innerIndex = outerIndex;
                }
            }
            return hasCurrent;
        }

        public Pair<O> next() {
            if (hasCurrent || hasNext()) {
                hasCurrent = false;
                return current;
            }
            throw new NoSuchElementException();
        }

        public void remove() {

        }

        public Iterator<Pair<O>> iterator() {
            return this;
        }


        public static class Pair<O> {
            O first;
            O second;
        }
    }
}


