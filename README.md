# OWLlink Semantic Matchmaking Extension

This project provides a reference Java implementation for an [OWLlink](http://www.owllink.org/) 
extension supporting non-standard inference services for semantic matchmaking. 
The extension has been integrated and tested successfully with the [Tiny-ME](http://swot.sisinflab.poliba.it/tinyme/)
cross-platform reasoner for Web, desktop, mobile and embedded environments.

## Specification

XML Schema specifications for the OWLlink extension implemented in this project can be found 
at the following locations:
* [Structural specification][spec]
* [HTTP/XML binding][binding]

### Inference services

The following non-standard inference services are included in the extension:
* Concept Abduction [1]
* Concept Contraction [1]
* Bonus [1]
* Concept Difference [2]
* Concept Covering [1]


### References

[1]  Scioscia, F., Ruta, M., Loseto, G., Gramegna, F., Ieva, S., Pinto, A., Di Sciascio, E.: Mini-ME 
matchmaker and reasoner for the Semantic Web of Things. In: Innovations, Developments, and Applications 
of Semantic Web and Information Systems, pp. 262-294, IGI Global (2018).

[2] Teege, G.: Making the Difference: A Subtraction Operation for Description Logics.
In: Proceedings of the Fourth International Conference on the Principles of
Knowledge Representation and Reasoning (KR94), pp. 540-550, ACM (1994).



### Copyright and License

Copyright (c) 2020 [SisInf Lab][swot], [Polytechnic University of Bari][poliba]

The OWLlink Semantic Matchmaking Extension is distributed under the [Eclipse Public License, Version 2.0][epl2].

[epl2]: https://www.eclipse.org/legal/epl-2.0
[poliba]: http://www.poliba.it
[swot]: http://sisinflab.poliba.it/swottools
[spec]: http://swot.sisinflab.poliba.it/reasoners/owllink-ext-matchmaking.xsd
[binding]: http://swot.sisinflab.poliba.it/reasoners/owllink-ext-matchmaking-20091128.xsd
