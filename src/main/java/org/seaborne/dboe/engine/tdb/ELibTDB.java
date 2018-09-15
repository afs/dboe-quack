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

package org.seaborne.dboe.engine.tdb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.atlas.iterator.Iter;
import org.apache.jena.atlas.lib.InternalErrorException;
import org.apache.jena.atlas.lib.tuple.Tuple;
import org.apache.jena.atlas.lib.tuple.TupleFactory;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.sparql.core.Quad;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.binding.Binding;
import org.seaborne.dboe.engine.Row;
import org.seaborne.dboe.engine.RowBuilder;
import org.seaborne.dboe.engine.Slot;
import org.apache.jena.tdb2.store.NodeId;
import org.apache.jena.tdb2.store.nodetable.NodeTable;

public class ELibTDB {
    public static List<Tuple<Slot<NodeId>>> convertTriples(List<Triple> triples, NodeTable nodeTable) {
        return convertTriples(triples, nodeTable, false);
    }
    
    public static List<Tuple<Slot<NodeId>>> convertTriples(List<Triple> triples, NodeTable nodeTable, boolean allocate) {
        List<Tuple<Slot<NodeId>>> x = new ArrayList<>();
        for ( Triple t : triples ) {
            Tuple<Slot<NodeId>> ts = apply(t, nodeTable, allocate);
            if ( ts == null ) 
                return null;
            x.add(ts);
        }
        return x;
    }

    public static List<Tuple<Slot<NodeId>>> convertQuads(List<Quad> quads, NodeTable nodeTable) {
        return convertQuads(quads, nodeTable, false);
    }
    
    public static List<Tuple<Slot<NodeId>>> convertQuads(List<Quad> quads, NodeTable nodeTable, boolean allocate) {
        List<Tuple<Slot<NodeId>>> x = new ArrayList<>();
        for ( Quad q : quads ) {
            Tuple<Slot<NodeId>> ts = apply(q, nodeTable, allocate);
            if ( ts == null ) 
                return null;
            x.add(ts);
        }
        return x;
    }

    public static List<Tuple<Slot<NodeId>>> convertQuads(Node graphNode, List<Triple> triples, NodeTable nodeTable) {
        return convertQuads(graphNode, triples, nodeTable, false);
    }
    
    public static List<Tuple<Slot<NodeId>>> convertQuads(Node graphNode, List<Triple> triples, NodeTable nodeTable, boolean allocate) {
        if ( graphNode == null || Quad.isDefaultGraph(graphNode) || Quad.isUnionGraph(graphNode) )
            throw new InternalErrorException("convertQuads sees unexpect graph node: "+graphNode); 
        List<Tuple<Slot<NodeId>>> x = new ArrayList<>();
        for ( Triple t : triples ) {
            Tuple<Slot<NodeId>> ts = apply(graphNode, t, nodeTable, allocate);
            if ( ts == null ) 
                return null;
            x.add(ts);
        }
        return x;
    }

    public static Tuple<Slot<NodeId>> apply(Triple triple, NodeTable nodeTable, boolean allocate) {
        @SuppressWarnings("unchecked")
        Slot<NodeId>[] slots = (Slot<NodeId>[])new Slot<?>[3];
        slots[0] = apply(triple.getSubject(), nodeTable, allocate);
        if (slots[0].term == NodeId.NodeDoesNotExist)
            return null;
        slots[1] = apply(triple.getPredicate(), nodeTable, allocate);
        if (slots[1].term == NodeId.NodeDoesNotExist)
            return null;
        slots[2] = apply(triple.getObject(), nodeTable, allocate);
        if (slots[2].term == NodeId.NodeDoesNotExist)
            return null;
        return TupleFactory.create(slots);
    }

    public static Tuple<Slot<NodeId>> apply(Node gn, Triple triple, NodeTable nodeTable, boolean allocate) {
        @SuppressWarnings("unchecked")
        Slot<NodeId>[] slots = (Slot<NodeId>[])new Slot<?>[4];
        slots[0] = apply(gn, nodeTable, allocate);
        if (slots[0].term == NodeId.NodeDoesNotExist)
            return null;
        slots[1] = apply(triple.getSubject(), nodeTable, allocate);
        if (slots[1].term == NodeId.NodeDoesNotExist)
            return null;
        slots[2] = apply(triple.getPredicate(), nodeTable, allocate);
        if (slots[2].term == NodeId.NodeDoesNotExist)
            return null;
        slots[3] = apply(triple.getObject(), nodeTable, allocate);
        if (slots[3].term == NodeId.NodeDoesNotExist)
            return null;
        return TupleFactory.create(slots);
    }

    /** Convert a quad to tuples, return null if a slot is known not to exist */ 
    public static Tuple<Slot<NodeId>> apply(Quad quad, NodeTable nodeTable, boolean allocate) {
        @SuppressWarnings("unchecked")
        Slot<NodeId>[] slots = (Slot<NodeId>[])new Slot<?>[4];
        slots[0] = apply(quad.getGraph(), nodeTable, allocate);
        if (slots[0].term == NodeId.NodeDoesNotExist)
            return null;
        slots[1] = apply(quad.getSubject(), nodeTable, allocate);
        if (slots[1].term == NodeId.NodeDoesNotExist)
            return null;
        slots[2] = apply(quad.getPredicate(), nodeTable, allocate);
        if (slots[2].term == NodeId.NodeDoesNotExist)
            return null;
        slots[3] = apply(quad.getObject(), nodeTable, allocate);
        if (slots[3].term == NodeId.NodeDoesNotExist)
            return null;
        return TupleFactory.create(slots);
    }
    
    public static Slot<NodeId> apply(Node node, NodeTable nodeTable, boolean allocate) {
        if (Var.isVar(node))
            return Slot.createVarSlot(Var.alloc(node));
        if (allocate)
            return Slot.createTermSlot(nodeTable.getAllocateNodeId(node));
        else
            return Slot.createTermSlot(nodeTable.getNodeIdForNode(node));
    }

    public static Tuple<Slot<NodeId>> apply(Triple t, NodeTable nodeTable) {
        @SuppressWarnings("unchecked")
        Slot<NodeId>[] slots = (Slot<NodeId>[])new Slot<?>[3]; 
        slots[0] =  apply(t.getSubject(), nodeTable);
        slots[1] =  apply(t.getPredicate(), nodeTable);
        slots[2] =  apply(t.getObject(), nodeTable);
        return TupleFactory.create(slots);
    }

    public static Slot<NodeId> apply(Node n, NodeTable nodeTable) {
        if ( Var.isVar(n)) 
            return Slot.createVarSlot(Var.alloc(n));
        // Miss?
        return Slot.createTermSlot(nodeTable.getNodeIdForNode(n));
    }

    public static Iterator<Binding> convertToBindings(Iterator<Row<NodeId>> iter, NodeTable nodeTable) {
        return Iter.map(iter, (row)->new BindingRow(row, nodeTable));
    }
    
    public static Binding convertToBinding(Row<NodeId> row, NodeTable nodeTable) {
        return new BindingRow(row, nodeTable);
    }
    
    public static Iterator<Row<NodeId>> convertToRows(Iterator<Binding> iter, 
                                                      NodeTable nodeTable, 
                                                      RowBuilder<NodeId> builder) {
        return Iter.map(iter, (binding)->convertToRow(binding, nodeTable, builder));
    }
    
    public static Row<NodeId> convertToRow(Binding binding, NodeTable nodeTable, RowBuilder<NodeId> builder) {
        if ( binding instanceof BindingRow )
            return ((BindingRow)binding).getRow();
        builder.reset();
        Iterator<Var> vIter = binding.vars(); 
        for (; vIter.hasNext(); ) {
            Var v = vIter.next();
            Node n = binding.get(v);
            if ( n == null )
                continue;
            NodeId nid = nodeTable.getNodeIdForNode(n);
            if ( NodeId.isDoesNotExist(nid) )
                // FIXME ???
                continue; 
            builder.add(v, nid);
        }
        return builder.build(); 
    }
    
    /** Create a tuple oflength 3 or 4 depending on g */ 
    public static Tuple<NodeId> createTuple(NodeId g, NodeId s, NodeId p, NodeId o) {
        if ( s == null ) s = NodeId.NodeIdAny;
        if ( p == null ) p = NodeId.NodeIdAny;
        if ( o == null ) o = NodeId.NodeIdAny;
        if ( g != null )
            return TupleFactory.tuple(g, s, p, o);
        else
            return TupleFactory.tuple(s, p, o);
    }

    public static NodeId slotToNodeId(Slot<NodeId> slot) {
        if ( slot.isVar() )
            return NodeId.NodeIdAny;
        else
            return slot.term;
    }
    
// Debug operations : 
    
//    public static List<Triple> convertToTriples(List<Tuple<Slot<NodeId>>> tuples, NodeTable nodeTable) {
//        List<Triple> x = new ArrayList<>()
//        for ( Tuple<Slot<NodeId>> ts : tuples )
//            x.add(convertToTriple(ts, nodeTable));
//        return x;
//    }
//    
//    public static Triple convertToTriple(Tuple<Slot<NodeId>> tuple, NodeTable nodeTable) {
//        if (tuple.size() != 3)
//            throw new TDBException("Tuple is not of length 3 : " + tuple);
//
//        return Triple.create(apply(tuple.get(0), nodeTable), apply(tuple.get(1), nodeTable),
//                             apply(tuple.get(2), nodeTable));
//    }
//
//    public static Quad convertToQuad(Tuple<Slot<NodeId>> tuple, NodeTable nodeTable) {
//        if (tuple.size() != 4)
//            throw new TDBException("Tuple is not of length 4 : " + tuple);
//
//        return Quad.create(apply(tuple.get(0), nodeTable), apply(tuple.get(1), nodeTable),
//                           apply(tuple.get(2), nodeTable), apply(tuple.get(3), nodeTable));
//    }

}

