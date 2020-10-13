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

package org.seaborne.dboe.engine.access;

import java.util.Iterator;

import org.apache.jena.atlas.lib.tuple.Tuple;
import org.seaborne.dboe.engine.PredicateObjectList;
import org.seaborne.dboe.engine.Row;
import org.seaborne.dboe.engine.Slot;


/** Combine all three access forms: AccessRows, AccessData, fetch(PredicateObjectList) */
public interface Accessor<X> extends AccessRows<X>, AccessData<X> {
    // -- AccessData
    /** Access by constant/wildcard  pattern to get tuples of data */ 
    @Override public Iterator<Tuple<X>> accessTuples(Tuple<X> pattern);
    
    //---- AccessRows
    /** Access data via the pattern, using builder to create rows.
     * @param  pattern (elements may not be null)
     * @return Rows, as an iterator
     */
    @Override
    public Iterator<Row<X>> accessRows(Tuple<Slot<X>> pattern);

    /** Access with a predicate-object list */ 
    @Override public Iterator<Row<X>> fetch(PredicateObjectList<X> accessList);
}
