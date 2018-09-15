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

package org.seaborne.dboe.engine;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/** Tests of variations of OpExecutor engines */
@RunWith(Suite.class)
@Suite.SuiteClasses( {
    TestOpExecutorNode.class
    , TestOpExecutorNodeId.class
} )

// It would be nice to run the std SPARQL scripted test suite multiple tie with different OpExecutorFactories.
// TC_SPARQL, TS_SPARQLTests

public class TS_QuackEngines
{ }

