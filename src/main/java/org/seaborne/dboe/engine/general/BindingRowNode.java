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

package org.seaborne.dboe.engine.general;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.BiConsumer;

import org.apache.jena.graph.Node;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.binding.Binding;
import org.seaborne.dboe.engine.Row;

/** Wrap a Row&lt;Node&gt; to present the Binding interface */
public final class BindingRowNode implements Binding {
    private final Row<Node> row;

    public BindingRowNode(Row<Node> row) { this.row = row; }

    @Override
    public void forEach(BiConsumer<Var, Node> action) {
        row.vars().forEach(v-> {
            Node n = row.get(v);
            action.accept(v, n);
        });
    }

    @Override
    public Iterator<Var> vars() {
        return row.vars().iterator();
    }

    @Override
    final public Set<Var> varsMentioned() {
        Set<Var> result = new LinkedHashSet<>();
        vars().forEachRemaining(result::add);
        return result;
    }

    @Override
    public boolean contains(Var var) {
        return row.contains(var);
    }

    @Override
    public Node get(Var var) {
        return row.get(var);
    }

    @Override
    public int size() {
        return row.vars().size();
    }

    @Override
    public boolean isEmpty() {
        return row.vars().size() == 0;
    }
}