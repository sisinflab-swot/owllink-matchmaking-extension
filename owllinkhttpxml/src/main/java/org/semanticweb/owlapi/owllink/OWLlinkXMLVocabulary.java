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

package org.semanticweb.owlapi.owllink;

import org.semanticweb.owlapi.model.IRI;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Olaf Noppens
 */
public enum OWLlinkXMLVocabulary {
    BUILD_ATTRIBUTE("build"),
    CONSIDER_IMPORTS_ATTRIBUTE("considerImports"),
    DIRECT_ATTRIBUTE("direct"),
    ERROR_ATTRIBUTE("error"),
    IDENTIFIER_ATTRIBUTE("identifier"),
    IRI_ATTRIBUTE("IRI"),
    IRI_MAPPING("IRIMapping"),
    FULL_IRI_ATTRIBUTE("fullIRI"),
    KB_ATTRIBUTE("kb"),
    KEY_ATTRIBUTE("key"),
    NAME_Attribute("name"),
    NEGATIVE_ATTRIBUTE("negative"),
    MAJOR_ATTRIBUTE("major"),
    MINOR_ATTRIBUTE("minor"),
    MESSAGE_ATTRIBUTE("message"),
    CONFIGURATION("Configuration"),
    REQUEST_MESSAGE("RequestMessage"),
    RESULT_ATTRIBUTE("result"),
    RESPONSE_MESSAGE("ResponseMessage"),
    WARNING_ATTRIBUTE("warning"),
    VALUE_ATTRIBUTE("value"),

    ARE_CLASSES_DISJOINT("AreClassesDisjoint"),
    ARE_CLASSES_EQUIVALENT("AreClassesEquivalent"),
    ARE_DATAPROPERTIES_DISJOINT("AreDataPropertiesDisjoint"),
    ARE_DATAPROPERTIES_EQUIVALENT("AreDataPropertiesEquivalent"),
    ARE_INDIVIDUALS_DISJOINT("AreIndividualsDisjoint"),
    ARE_INDIVIDUALS_EQUIVALENT("AreIndividualsEquivalent"),
    ARE_INDIVIDUALS_RELATED("AreIndividualsRelated"),
    ARE_OBJECTPROPERTIES_DISJOINT("AreObjectPropertiesDisjoint"),
    ARE_OBJECTPROPERTIES_EQUIVALENT("AreDataPropertiesEquivalent"),
    CLASSIFY("Classify"),
    CREATE_KB("CreateKB"),
    DESCRIPTION("Description"),
    FULLIRI("fullIRI"),
    GET_ALL_ANNOTATION_PROPERTIES("GetAllAnnotationProperties"),
    GET_ALL_CLASSES("GetAllClasses"),
    GET_ALL_DATAPROPERTIES("GetAllDataProperties"),
    GET_ALL_DATATYPES("GetAllDatatypes"),
    GET_ALL_INDIVIDUALS("GetAllIndividuals"),
    GET_ALL_OBJECTPROPERTIES("GetAllObjectProperties"),
    GET_DATAPROPERTIES_BETWEEN("GetDataPropertiesBetween"),
    GET_DATAPROPERTIES_OF_LITERAL("GetDataPropertiesOfLiteral"),
    GET_DATAPROPERTIES_OF_SOURCE("GetDataPropertiesOfSource"),
    GET_DATAPROPERTY_SOURCES("GetDataPropertySources"),
    GET_DATAPROPERTY_TARGETS("GetDataPropertyTargets"),
    GET_DESCRIPTION("GetDescription"),
    GET_DISJOINT_CLASSES("GetDisjointClasses"),
    GET_DISJOINT_DATAPROPERTIES("GetDisjointDataProperties"),
    GET_DIFFERENT_INDIVIDUALS("GetDifferentIndividuals"),
    GET_DISJOINT_OBJECTPROPERTIES("GetDisjointObjectProperties"),
    GET_EQUIVALENT_CLASSES("GetEquivalentClasses"),
    GET_EQUIVALENT_DATAPROPERTIES("GetEquivalentDataProperties"),
    GET_SAME_INDIVIDUALS("GetSameIndividuals"),
    GET_EQUIVALENT_OBJECTPROPERTIES("GetEquivalentObjectProperties"),
    GET_FLATTENED_DATAPROPERTY_SOURCES("GetFlattenedDataPropertySources"),
    GET_FLATTENED_DIFFERENT_INDIVIDUALS("GetFlattenedDifferentIndividuals"),
    GET_FLATTENED_INSTANCES("GetFlattenedInstances"),
    GET_FLATTENED_OBJECTPROPERTY_SOURCES("GetFlattenedObjectPropertySources"),
    GET_FLATTENED_OBJECTPROPERTY_TARGETS("GetFlattenedObjectPropertyTargets"),
    GET_FLATTENED_TYPES("GetFlattenedTypes"),
    GET_INSTANCES("GetInstances"),
    GET_KB_LANGUAGE("GetKBLanguage"),
    GET_PREFIXES("GetPrefixes"),
    GET_OBJECTPROPERTIES_BETWEEN("GetObjectPropertiesBetween"),
    GET_OBJECTPROPERTIES_OF_SOURCE("GetObjectPropertiesOfSource"),
    GET_OBJECTPROPERTIES_OF_TARGET("GetObjectPropertiesOfTarget"),
    GET_OBJECTPROPERTY_SOURCES("GetObjectPropertySources"),
    GET_OBJECTPROPERTY_TARGETS("GetObjectPropertyTargets"),
    GET_SETTINGS("GetSettings"),
    GET_SUBCLASSES("GetSubClasses"),
    GET_SUBCLASS_HIERARCHY("GetSubClassHierarchy"),
    GET_SUBDATAPROPERTIES("GetSubDataProperties"),
    GET_SUBDATAPROPERTY_HIERARCHY("GetSubDataPropertyHierarchy"),
    GET_SUBOBJECTPROPERTIES("GetSubObjectProperties"),
    GET_SUBOBJECTPROPERTY_HIERARCHY("GetSubObjectPropertyHierarchy"),
    GET_SUPERCLASSES("GetSuperClasses"),
    GET_SUPERDATAPROPERTIES("GetSuperDataProperties"),
    GET_SUPEROBJECTPROPERTIES("GetSuperObjectProperties"),
    GET_TYPES("GetTypes"),
    IS_CLASS_SATISFIABLE("IsClassSatisfiable"),
    IS_ENTAILED("IsEntailed"),
    IS_ENTAILED_DIRECT("IsEntailedDirect"),
    IS_KB_CONSISTENTLY_DECLARED("IsKBConsistentlyDeclared"),
    IS_DATAPROPERTY_SATISFIABLE("IsDataPropertySatisfiable"),
    IS_OBJECTPROPERTY_SATISFIABLE("IsObjectPropertySatisfiable"),
    IS_KB_SATISFIABLE("IsKBSatisfiable"),
    KBERROR("KBError"),
    LOAD_ONTOLOGIES("LoadOntologies"),
    NOTSUPPORTEDDATATYPEERROR("NotSupportedDatatypeError"),
    ONTOLOGY_IRI("OntologyIRI"),
    PROFILEVIOLATIONERROR("ProfileViolationError"),
    REALIZE("Realize"),
    RELEASE_KB("ReleaseKB"),
    SET("Set"),
    TELL("Tell"),
    SETTING("Setting"),
    SETTINGS("Settings"),
    UNSATISFIABLEKBERROR("UnsatisfiableKBError"),
    PREFIX("Prefix"),
    PROPERTY("Property"),
    PublicKB("PublicKB"),
    SUPPORTEDEXTENSION("SupportedExtension"),
    LITERAL("Literal"),
    ONEOF("OneOf"),
    LIST("List"),
    DATATYPE("Datatype"),
    PROTOCOLVERSION("ProtocolVersion"),
    REASONERVERSION("ReasonerVersion"),

    BOOLEAN_RESPONSE("BooleanResponse"),
    CLASSES("Classes"),
    CLASS_HIERARCHY("ClassHierarchy"),
    CLASS_SUBCLASSESPAIR("ClassSubClassesPair"),
    CLASS_SYNSET("ClassSynset"),
    CLASS_SYNSETS("ClassSynsets"),
    DATAPROPERTY_HIERARCHY("DataPropertyHierarchy"),
    DATAPROPERTY_SUBDATAPROPERTIESPAIR("DataPropertySubDataPropertiesPair"),
    DATAPROPERTY_SYNSET("DataPropertySynset"),
    DATAPROPERTY_SYNSETS("DataPropertySynsets"),
    DATAPROPERTY_SYNONYMS("DataPropertySynonyms"),
    ERROR("Error"),
    INDIVIDUAL_SYNSET("IndividualSynset"),
    INDIVIDUAL_SYNONYMS("IndividualSynonyms"),
    KB_ERROR("KBError"),
    KB_RESPONSE("KB"),
    OBJECTPROPERTY_HIERARCHY("ObjectPropertyHierarchy"),
    OBJECTPROPERTY_SUBOBJECTPROPERTIESPAIR("ObjectPropertySubObjectPropertiesPair"),
    OBJECTPROPERTY_SYNSET("ObjectPropertySynset"),
    OBJECTPROPERTY_SYNSETS("ObjectPropertySynsets"),
    OK("OK"),
    PREFIXES("Prefixes"),
    SET_OF_ANNOTATIONPROPERTIES("SetOfAnnotationProperties"),
    SET_OF_CLASSES("SetOfClasses"),
    SET_OF_CLASS_SYNSETS("SetOfClassSynsets"),
    SET_OF_DATAPROPERTIES("SetOfDataProperties"),
    SET_OF_DATAPROPERTY_SYNSETS("SetOfDataPropertySynsets"),
    SET_OF_DATATYPES("SetOfDatatypes"),
    SET_OF_INDIVIDUALS("SetOfIndividuals"),
    SET_OF_INDIVIDUALS_SYNSETS("SetOfIndividualSynsets"),
    SET_OF_LITERALS("SetOfLiterals"),
    SET_OF_OBJECTPROPERTIES("SetOfObjectProperties"),
    SET_OF_OBJECTPROPERTY_SYNSETS("SetOfObjectPropertySynsets"),
    SUBCLASS_SYNSETS("SubClassSynsets"),
    SUBDATAPROPERTY_SYNSETS("SubDataPropertySynsets"),
    SUBOBJECTPROPERTY_SYNSETS("SubObjectPropertySynsets"),
    UNKNOWN_RESPONSE("Unknown"),
    SYNTAX_ERROR("SyntaxError"),
    SEMANTIC_ERROR("SemanticError"),
    String_RESPONSE("StringResponse"),
    /*Non standard inference*/
    NORM("Norm"),
    CLASS_EXPRESSION_WITH_NORM("ClassExpressionWithNorm"),
    PAIR_OF_CLASS_EXPRESSION_WITH_NORM("PairOfClassExpressionWithNorm"),
    SET_OF_INDIVIDUALS_WITH_CLASS_EXPRESSION_AND_NORM("SetOfIndividualsWithClassExpressionAndNorm"),
    GET_ABDUCTION("GetAbduction"),
    GET_BONUS("GetBonus"),
    GET_DIFFERENCE("GetDifference"),
    GET_CONTRACTION("GetContraction"),
    GET_COVERING("GetCovering");
    private IRI iri;

    private String shortName;


    OWLlinkXMLVocabulary(String name) {
        this.iri = IRI.create(OWLlinkNamespaces.OWLLink + "#" + name);
        shortName = name;
    }

    public IRI getIRI() {
        return iri;
    }

    public URI getURI() {
        return iri.toURI();
    }


    public String getShortName() {
        return shortName;
    }


    public String toString() {
        return iri.toString();
    }


    static Set<URI> BUILT_IN_URIS;


    static {
        BUILT_IN_URIS = new HashSet<URI>();
        for (OWLlinkXMLVocabulary v : OWLlinkXMLVocabulary.values()) {
            BUILT_IN_URIS.add(v.getURI());
        }
    }

}
