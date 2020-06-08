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

## Usage

The extension can be built and run on **Windows**, **macOS** and **Linux** and can be launched via its [command line interface]

### Building

You can build extension via the included [Gradle](https://gradle.org) wrapper. 
`git clone` this project and the run `./gradlew jar`

#### Library usage

The OWLlinkMatchmakingReasonerBridge class must be extended, overriding all methods declared by OWLlinkMatchmakingReasonerBridgeInterface. 

```java
public class MyReasonerOWLlinkBridge extends OWLlinkMatchmakingReasonerBridge{
    void getAbduction(KBRequestWithTwoExpressionOrIndividuals request){}
    void getBonus(KBRequestWithTwoExpressionOrIndividuals request){}
    void getDifference(KBRequestWithTwoExpressionOrIndividuals request){}
    void getContraction(KBRequestWithTwoExpressionOrIndividuals request){}
    void getCovering(GetCovering request){}
}

```
Then the OWLlinkHTTPXMLServer interface must be implemented as follows. 
```java
public class MyReasonerServerFactory implements OWLlinkServerFactory {

    public OWLlinkHTTPXMLServer createServer(int port) throws OWLRuntimeException {
        SimpleConfiguration configuration = new SimpleConfiguration() {
            @Override
            public IndividualNodeSetPolicy getIndividualNodeSetPolicy() {
                return IndividualNodeSetPolicy.BY_SAME_AS;
            }
        };
        AbstractOWLlinkReasonerConfiguration config = new AbstractOWLlinkReasonerConfiguration();

        config.setSupportedDatatypes(OWL2Datatype.XSD_LONG.getIRI(),
                OWL2Datatype.XSD_INT.getIRI(),
                OWL2Datatype.XSD_NON_POSITIVE_INTEGER.getIRI(),
                OWL2Datatype.XSD_NON_NEGATIVE_INTEGER.getIRI(),
                OWL2Datatype.XSD_SHORT.getIRI(),
                OWL2Datatype.OWL_REAL.getIRI());
        try {
            Class c = Class.forName("full package class path");
            OWLReasonerFactory factory = (OWLReasonerFactory) c.newInstance();
            OWLlinkHTTPXMLServer server = new OWLlinkHTTPXMLServer(factory, config, port, new MyReasonerOWLlinkBridge(factory, config));
            return server;
        } catch (Exception e) {
            throw new OWLRuntimeException(e);
        }
    }

    static void usage() {
       ...
    }


    public static void main(String args[]) {
        int port = 8080;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (arg.equalsIgnoreCase("-help")) {
                usage();
                System.exit(0);
            } else if (arg.equalsIgnoreCase("-port")) {
                try {
                    port = Integer.parseInt(args[++i]);
                } catch (NumberFormatException e1) {
                    System.err.println("Invalid port number: " + args[i]);
                    System.exit(1);
                }
            } else {
                System.err.println("Unrecognized option: " + arg);
                usage();
                System.exit(1);
            }
        }
        MyReasonerServerFactory factory = new MyReasonerServerFactory();

        OWLlinkHTTPXMLServer server = factory.createServer(port);
        server.run();
    }
}
```
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
