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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.jena.sparql.core.Var;
import org.seaborne.dboe.engine.Row;
import org.seaborne.dboe.engine.RowList;

/** The join identity RowList : one row, no variables */ 
public class RowListEmpty<X> implements RowList<X>
{
    @SuppressWarnings("rawtypes")
    private static RowListEmpty instance = new RowListEmpty<>(); 
    
    @SuppressWarnings({"unchecked", "cast"})
    public static <Z> RowList<Z> emptyRowList() { return (RowList<Z>)instance; }
    
    private static Set<Var> vars = Collections.emptySet();
    
    @Override
    public String toString() { return "RowList"+vars; }

    @Override
    public Set<Var> vars() { return Collections.emptySet(); }

    @Override
    public List<Row<X>> toList() { return Collections.emptyList(); }

    @Override
    public RowList<X> materialize() {
        return this;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean isIdentity() {
        return false;
    }

    @Override
    public Iterator<Row<X>> iterator() {
        return toList().iterator();
    }
}
