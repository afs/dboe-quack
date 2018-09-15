/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  See the NOTICE file distributed with this work for additional
 *  information regarding copyright ownership.
 */

package org.seaborne.dboe.sparql;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.seaborne.dboe.sparql.test.Label;
import org.seaborne.dboe.sparql.test.Manifests;
import org.seaborne.dboe.sparql.test.RunnerSPARQL;

import org.apache.jena.sparql.expr.E_Function;
import org.apache.jena.sparql.expr.NodeValue;

@RunWith(RunnerSPARQL.class)
@Label("SPARQL [main]")
@Manifests
  ({
      // DAWG
      "/home/afs/Jena/jena-arq/testing/DAWG-Final/manifest-syntax.ttl",
      "/home/afs/Jena/jena-arq/testing/DAWG-Final/manifest-evaluation.ttl",
      
//      // SPARQL-WG
//      // Strict mode needed
//      "/home/afs/Desktop/sparql11-test-suite/manifest-arq-sparql11.ttl",
//      "/home/afs/Jena/jena-arq/testing/DAWG-Final/manifest-evaluation.ttl",

      // ARQ
      // Not strict mode.
      "/home/afs/Jena/jena-arq/testing/ARQ/Serialization/manifest.ttl",
      "/home/afs/Jena/jena-arq/testing/ARQ/Syntax/manifest-syntax.ttl",
      "/home/afs/Jena/jena-arq/testing/ARQ/manifest-arq.ttl",
      "/home/afs/Jena/jena-arq/testing/ARQ/manifest-ref-arq.ttl",
  })

public class TS_SPARQLTests
{ 
    @BeforeClass static public void beforeClass() {
        NodeValue.VerboseWarnings = false;
        E_Function.WarnOnUnknownFunction = false;
    }  
    
    @AfterClass static public void afterClass() {
        NodeValue.VerboseWarnings = true;
        E_Function.WarnOnUnknownFunction = true;
    }
}
