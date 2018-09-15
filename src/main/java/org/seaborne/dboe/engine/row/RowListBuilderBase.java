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

package org.seaborne.dboe.engine.row;

import java.util.*;

import org.apache.jena.atlas.iterator.Iter;
import org.apache.jena.atlas.logging.Log;
import org.apache.jena.sparql.core.Var;
import org.seaborne.dboe.engine.Row;
import org.seaborne.dboe.engine.RowList;
import org.seaborne.dboe.engine.RowListBuilder;

/** General RowListBuilder */
public class RowListBuilderBase<X> implements RowListBuilder<X>{
    private Set<Var> vars = new HashSet<>();
    private List<Row<X>> rows = new ArrayList<>();
    private boolean valid = true;
    
    public RowListBuilderBase() {}
    
    @Override
    public RowListBuilderBase<X> add(Row<X> row) {
        vars.addAll(row.vars());
        rows.add(row);
        return this;
    }
    
    @Override
    public RowList<X> build() {
        if ( ! valid ) {
            Log.warn(this, ".build() called on invalid build", null);
            return null;
        }
        return new RowListBase<X>(vars, rows.iterator());
    }

    public static <X> RowList<X> empty() {
        Iterator<Row<X>> iter0 = Iter.nullIterator();
        Set<Var> vars = Collections.emptySet();
        return new RowListBase<X>(vars, iter0);
    }

    @Override
    public void markInvalid() { valid = false; }

    @Override
    public boolean isInvalid() {
        return valid;
    }
}
