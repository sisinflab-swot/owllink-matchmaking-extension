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

package org.semanticweb.owlapi.owllink.server.parser;

import org.coode.owlapi.owlxmlparser.OWLElementHandler;
import org.coode.owlapi.owlxmlparser.OWLXMLParserException;
import org.coode.owlapi.owlxmlparser.OWLXMLParserHandler;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary;
import org.semanticweb.owlapi.owllink.PrefixManagerProvider;
import org.semanticweb.owlapi.owllink.Request;
import org.semanticweb.owlapi.owllink.parser.MyOWLXMLParserHandler;
import org.semanticweb.owlapi.owllink.retraction.RetractionVocabulary;
import org.semanticweb.owlapi.owllink.retraction.server.OWLlinkRetractElementHandler;
import org.semanticweb.owlapi.vocab.Namespaces;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Olaf Noppens
 * Date: 25.10.2009
 */
public class OWLlinkXMLRequestParserHandler extends MyOWLXMLParserHandler {

    protected Map<String, OWLlinkElementHandlerFactory> owllinkHandlerMap;
    protected PrefixManagerProvider prov;


    public OWLlinkXMLRequestParserHandler(OWLOntology ontology, PrefixManagerProvider prov) {
        this(ontology, prov, null);
    }

    public OWLlinkXMLRequestParserHandler(OWLOntology ontology, PrefixManagerProvider prov, OWLElementHandler topHandler) {
        super(ontology, topHandler);
        owllinkHandlerMap = new HashMap<String, OWLlinkElementHandlerFactory>();
        this.prov = prov;

        addFactory(new AbstractOWLlinkElementHandlerFactory(RetractionVocabulary.Retraction.getShortName()) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkRetractElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.CLASSIFY) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkClassifyElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_ALL_ANNOTATION_PROPERTIES) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetAllAnnotationPropertiesElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_ALL_CLASSES) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetAllClassesElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_ALL_DATAPROPERTIES) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetAllDataPropertiesElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_ALL_DATATYPES) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetAllDatatypesElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_ALL_INDIVIDUALS) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetAllIndividualsElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_ALL_OBJECTPROPERTIES) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetAllObjectPropertiesElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.CREATE_KB) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkCreateKBElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_DATAPROPERTIES_OF_SOURCE) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetDataPropertiesOfSourceElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_DATAPROPERTIES_BETWEEN) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetDataPropertiesBetweenElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_DATAPROPERTIES_OF_LITERAL) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetDataPropertiesOfLiteralElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_DATAPROPERTY_TARGETS) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetDataPropertyTargetsElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_DATAPROPERTY_SOURCES) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetDataPropertySourcesElementHandler(handler);
            }
        });


        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_DESCRIPTION) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetDescriptionElemenHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_DIFFERENT_INDIVIDUALS) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetDifferentIndividualsElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_DISJOINT_CLASSES) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetDisjointClassesElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_DISJOINT_DATAPROPERTIES) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetDisjointDataPropertiesElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_DISJOINT_OBJECTPROPERTIES) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetDisjointObjectPropertiesElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_EQUIVALENT_CLASSES) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetEquivalentClassesElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_EQUIVALENT_DATAPROPERTIES) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetEquivalentDataPropertiesElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_FLATTENED_DATAPROPERTY_SOURCES) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetFlattenedDataPropertySourcesElementHandler(handler);
            }
        });


        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_EQUIVALENT_OBJECTPROPERTIES) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetEquivalentObjectPropertiesElementHandler(handler);
            }
        });


        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_KB_LANGUAGE) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetKBLanguageElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_FLATTENED_DIFFERENT_INDIVIDUALS) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetFlattenedDifferentIndividualsElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_FLATTENED_INSTANCES) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetFlattenedInstancesElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_FLATTENED_OBJECTPROPERTY_TARGETS) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetFlattenedObjectPropertyTargetsElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_FLATTENED_OBJECTPROPERTY_SOURCES) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetFlattendObjectPropertySourcesElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_INSTANCES) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetInstancesElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_OBJECTPROPERTY_SOURCES) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetObjectPropertySourcesElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_OBJECTPROPERTY_TARGETS) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetObjectPropertyTargetsElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_OBJECTPROPERTIES_BETWEEN) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetObjectPropertiesBetweenElementHandler(handler);
            }
        });


        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_OBJECTPROPERTIES_OF_SOURCE) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetObjectPropertiesOfSourceElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_OBJECTPROPERTIES_OF_TARGET) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetObjectPropertiesOfTargetElementHandler(handler);
            }
        });


        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_PREFIXES) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetPrefixesElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_SAME_INDIVIDUALS) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetSameIndividualsElementHandler(handler);
            }
        });


        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_SETTINGS) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetSettingsElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_SUBCLASSES) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetSubClassesElementHandler(handler);
            }

        });


        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_SUBCLASS_HIERARCHY) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetSubClassHierarchyElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_SUBOBJECTPROPERTY_HIERARCHY) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetSubObjectPropertyHierarchyElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_SUBDATAPROPERTIES) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetSubDataPropertiesElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_SUBOBJECTPROPERTIES) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetSubObjectPropertiesElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_SUPERCLASSES) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetSuperClassesElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_SUBDATAPROPERTY_HIERARCHY) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetSubDataPropertyHierarchyElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_SUPERDATAPROPERTIES) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetSuperDataPropertiesElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_SUPEROBJECTPROPERTIES) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetSuperObjectPropertiesElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_TYPES) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetTypesElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_FLATTENED_TYPES) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetFlattenedTypesElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.IRI_MAPPING) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkIRIMappingElementHandler(handler);
            }
        });


        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.IS_CLASS_SATISFIABLE) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkIsClassSatisfiableElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.IS_DATAPROPERTY_SATISFIABLE) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkIsDataPropertySatisfiableElementHandler(handler);
            }
        });
        //

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.IS_KB_SATISFIABLE) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkIsKBSatisfiableElementHandler(handler);
            }
        });


        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.IS_KB_CONSISTENTLY_DECLARED) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkIsKBConsistentlyDeclaredElementHandler(handler);
            }
        });


        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.IS_ENTAILED) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkIsEntailedElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.IS_ENTAILED_DIRECT) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkIsEntailedDirectElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.ONTOLOGY_IRI) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkOntologyIRIElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.IS_OBJECTPROPERTY_SATISFIABLE) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkIsObjectPropertySatisfiableElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.LITERAL) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkLiteralElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.PREFIX) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkPrefixElementHandler(handler);
            }
        });


        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.LOAD_ONTOLOGIES) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkLoadOntologiesElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.RELEASE_KB) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkReleaseKBElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.SET) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkSetElementHandler(handler);
            }
        });


        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.TELL) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkTellElementHandler(handler);
            }
        });

        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.REALIZE) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkRealizeElementHandler(handler);
            }
        });


        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.REQUEST_MESSAGE) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return messageHandler;
            }
        });
    }

    OWLlinkRequestMessageElementHandler messageHandler = new OWLlinkRequestMessageElementHandler(this);

    public List<Request> getRequest() {
        try {
            return messageHandler.getOWLObject();
        } catch (OWLXMLParserException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public void setRequestListener(OWLLinkRequestListener listener) {
        this.messageHandler.setRequestListener(listener);
    }

    public void addFactory(OWLlinkElementHandlerFactory factory, String... legacyElementNames) {
        this.owllinkHandlerMap.put(factory.getElementName(), factory);
        for (String elementName : legacyElementNames) {
            this.owllinkHandlerMap.put(elementName, factory);
        }
    }


    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
         {
            processXMLBase(attributes);

            if (Namespaces.OWL2.toString().equals(uri) || Namespaces.OWL.toString().equals(uri) || Namespaces.OWL11XML.toString().equals(uri)) {
                super.startElement(uri, localName, qName, attributes);
            } else {
                OWLlinkElementHandlerFactory handlerFactory = owllinkHandlerMap.get(localName);
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


}
