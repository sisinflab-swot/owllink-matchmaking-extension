/*
 * This file is part of the OWLlink API.
 *
 * The contents of this file are subject to the LGPL License, Version 3.0.
 *
 * Copyright (C) 2011, derivo GmbH
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

package org.semanticweb.owlapi.owllink.parser;

import org.coode.owlapi.owlxmlparser.OWLElementHandler;
import org.coode.owlapi.owlxmlparser.OWLElementHandlerFactory;
import org.coode.owlapi.owlxmlparser.OWLXMLParserHandler;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary;
import org.semanticweb.owlapi.owllink.PrefixManagerProvider;
import org.semanticweb.owlapi.owllink.Request;
import org.semanticweb.owlapi.vocab.Namespaces;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handler for OWLlink Responses.
 * <p/>
 * Here we also handle the prefix name mapping for abbreviated IRIs.
 */
public class OWLlinkXMLParserHandler extends MyOWLXMLParserHandler {

    protected Map<String, OWLlinkElementHandlerFactory> owllinkHandlerMap;
    protected OWLlinkResponseMessageElementHandler responseMessageHandler = new OWLlinkResponseMessageElementHandler(this);
    protected PrefixManagerProvider prov;
    Request[] requests;

    public OWLlinkXMLParserHandler(OWLOntology ontology, PrefixManagerProvider prov, Request[] requests) {
        this(ontology, prov, requests, null);
    }

    public OWLlinkXMLParserHandler(OWLOntology ontology, PrefixManagerProvider provider, Request[] requests, OWLElementHandler topHandler) {
        super(ontology, topHandler);
        this.owllinkHandlerMap = new HashMap<String, OWLlinkElementHandlerFactory>();
        this.prov = provider;
        this.requests = requests;


        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.DESCRIPTION) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkDescriptionElementHandler(handler);
            }
        });
        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.PublicKB) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkPublicKBElementHandler(handler);
            }
        });
        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.SUPPORTEDEXTENSION) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkSupportedExtensionElemenetHandler(handler);
            }
        });
        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.PROPERTY) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkPropertyElementHandler(handler);
            }
        });
        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.SETTING) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkSettingElementHandler(handler);
            }
        });
        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.LITERAL) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkLiteralElementHandler(handler);
            }
        });
        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.ONEOF) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkOneOfElementHandler(handler);
            }
        });
        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.LIST) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkListElementHandler(handler);
            }
        });
        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.DATATYPE) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkDatatypeElementHandler(handler);
            }
        });
        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.PROTOCOLVERSION) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkProtocolVersionElementHandler(handler);
            }
        });
        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.REASONERVERSION) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkReasonerVersionElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.PREFIXES) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkPrefixesElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.KB_RESPONSE) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkKBElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.OK) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkOKElementHandler(handler);
            }
        });


        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.BOOLEAN_RESPONSE) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkBooleanResponseElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.UNKNOWN_RESPONSE) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkUnknownResponseElementHandler(handler);
            }
        });


        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.SET_OF_ANNOTATIONPROPERTIES) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkSetOfAnnotationPropertiesElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.SET_OF_CLASSES) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkSetOfClassesElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.SET_OF_DATAPROPERTIES) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkSetOfDataPropertiesElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.SET_OF_DATATYPES) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkSetOfDatatypesElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.SET_OF_INDIVIDUALS) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkSetOfIndividualsElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.SET_OF_OBJECTPROPERTIES) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkSetOfObjectPropertiesElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.String_RESPONSE) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkStringResponseElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.SET_OF_CLASS_SYNSETS) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkSetOfClassSynsetsElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.CLASSES) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkClassesElementHandler(handler);
            }
        });


        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.CLASS_SYNSET) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkClassSynsetElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.CLASS_SYNSETS) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkClassSynsetsElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.CLASS_HIERARCHY) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkClassHierarchyElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.CLASS_SUBCLASSESPAIR) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkClassSubClassesPairElementHandler(handler);
            }
        });


        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.SUBCLASS_SYNSETS) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkSubClassSynsetsElementHandler(handler);
            }
        });


        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.SET_OF_OBJECTPROPERTY_SYNSETS) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkSetOfObjectPropertySynsetsElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.OBJECTPROPERTY_SYNSET) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkObjectPropertySynsetElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.OBJECTPROPERTY_SYNSETS) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkObjectPropertySynsetsElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.OBJECTPROPERTY_HIERARCHY) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkObjectPropertyHierarchyElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.OBJECTPROPERTY_SUBOBJECTPROPERTIESPAIR) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkObjectPropertySubPropertiesPairElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.OBJECTPROPERTY_SUBOBJECTPROPERTIESPAIR) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkObjectPropertySubPropertiesPairElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.SUBOBJECTPROPERTY_SYNSETS) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkSubObjectPropertySynsetsElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.SET_OF_DATAPROPERTY_SYNSETS) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkSetOfDataPropertySynsetsElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.DATAPROPERTY_SYNSET) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkDataPropertySynsetElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.DATAPROPERTY_SYNSETS) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkDataPropertySynsetsElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.DATAPROPERTY_SYNONYMS) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkDataPropertySynonymsElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.DATAPROPERTY_HIERARCHY) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkDataPropertyHierarchyElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.DATAPROPERTY_SUBDATAPROPERTIESPAIR) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkDataPropertySubDataPropertiesPairElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.DATAPROPERTY_SUBDATAPROPERTIESPAIR) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkDataPropertySubDataPropertiesPairElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.SUBDATAPROPERTY_SYNSETS) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkSubDataPropertySynsetsElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.SET_OF_INDIVIDUALS_SYNSETS) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkSetOfIndividualSynsetsElementHandler(handler);
            }
        });
        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.INDIVIDUAL_SYNSET) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkIndividualSynsetElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.INDIVIDUAL_SYNONYMS) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkIndividualSynonymsElementHandler(handler);
            }
        });
        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.SET_OF_LITERALS) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkSetOfLiteralsElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.RESPONSE_MESSAGE) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return responseMessageHandler;
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.ERROR) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkErrorResponseElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.KBERROR) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkKBErrorElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.NOTSUPPORTEDDATATYPEERROR) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkNotSupportedDatatypeErrorElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.PROFILEVIOLATIONERROR) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkProfileViolationResponseErrorExceptionElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.SEMANTIC_ERROR) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkSemanticErrorElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.SYNTAX_ERROR) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkSyntaxErrorElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.UNSATISFIABLEKBERROR) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkUnsatisfiableKBErrorElementHandler(handler);
            }
        });


        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.PREFIX) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkPrefixElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.PREFIXES) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkPrefixesElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.SETTINGS) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkSettingsElementHandler(handler);
            }
        });
    }

    public void addFactory(OWLlinkElementHandlerFactory factory, String... legacyElementNames) {
        this.owllinkHandlerMap.put(factory.getElementName(), factory);
        for (String elementName : legacyElementNames) {
            this.owllinkHandlerMap.put(elementName, factory);
        }
    }

    public void addFactories(Collection<OWLlinkElementHandlerFactory> factories) {
        for (OWLlinkElementHandlerFactory factory : factories)
            this.owllinkHandlerMap.put(factory.getElementName(), factory);
    }


    public Request getRequest(int index) {
        return this.requests[index];
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        /*try */{
            processXMLBase(attributes);
            if (Namespaces.OWL2.toString().equals(uri) || Namespaces.OWL.toString().equals(uri) || Namespaces.OWL11XML.toString().equals(uri)) {
                super.startElement(uri, localName, qName, attributes);
            } else {
                OWLElementHandlerFactory handlerFactory = owllinkHandlerMap.get(localName);
                if (handlerFactory != null) {
                    OWLElementHandler handler = handlerFactory.createHandler(this);
                    if (!handlerStack.isEmpty()) {
                        OWLElementHandler topElement = handlerStack.get(0);
                        handler.setParentHandler(topElement);
                    }
                    handlerStack.add(0, handler);
                    handler.startElement(localName);

                    for (int i = 0; i < attributes.getLength(); i++) {
                        handler.attribute(attributes.getLocalName(i), attributes.getValue(i));
                    }
                }
            }
        }
        /*catch (OWLException e) {
            throw new SAXException(e.getMessage() + "(Current element " + localName + ")", e);
        }*/
    }

    public List<Object> getResponses() {
        return responseMessageHandler.getOWLLinkObject();
    }

}
