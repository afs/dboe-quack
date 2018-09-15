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

package dev;

import java.util.function.Function;

import org.apache.jena.atlas.lib.tuple.Tuple;
import org.apache.jena.atlas.lib.tuple.TupleFactory;
import org.apache.jena.atlas.lib.tuple.TupleN;
import org.seaborne.dboe.engine.Row;
import org.seaborne.dboe.engine.Slot;

/** A tuple of slots - a pattern, involving terms and variables */
public class Slots<X> extends TupleN<Slot<X>> {
    @SafeVarargs
    public static <X> Slots<X> create(Slot<X> ... slots) {
        return new Slots<X>(slots);
    }
    
    protected Slots(@SuppressWarnings("unchecked") Slot<X>... tuple) {
        super(tuple);
    }
    
    /** Substitute from row */ 
    public Slots<X> subst(Row<X> row) { 
     // Replace Slot var with any
        final int N = len();
        @SuppressWarnings("unchecked")
        Slot<X>[] converted = (Slot<X>[])new Object[N];
        for ( int i = 0; i < N; i++ ) {
            Slot<X> x = get(i);
            if ( x.isVar() ) {
                X elt = row.get(x.var);
                if ( elt != null )
                    x = Slot.createTermSlot(elt);
            }
            converted[i] = x;
        }
        return new Slots<X>(converted);
    }
    
    /** Substitute from row and if still a varibale, replace with the "any value"
     *  Same as {@code .subst(row).replaceVar(anyValue)} but more efficient. 
     */
    public Tuple<X> substReplace(Row<X> row, X anyValue) { 
        // Replace Slot var with any
           final int N = len();
           @SuppressWarnings("unchecked")
           X[] converted = (X[])new Object[N];
           for ( int i = 0; i < N; i++ ) {
               Slot<X> x = get(i);
               if ( x.isVar() ) {
                   X elt = row.get(x.var);
                   if ( elt != null )
                       x = Slot.createTermSlot(elt);
               }
               converted[i] = (x.isVar())? anyValue : x.term;
           }
           return TupleFactory.create(converted);
       }
    
    /** apply a tranformation to the tuple pattern */
    public <Z> Tuple<Z> mapToTuple(Function<Slot<X>,Z> transform) {
        final int N = len();
        @SuppressWarnings("unchecked")
        Z[] converted = (Z[])new Object[N];
        for ( int i = 0; i < N; i++ ) {
            Slot<X> x = get(i);
            converted[i] = transform.apply(x);
        }
        return TupleFactory.tuple(converted);
    }

    /** apply a tranformation to the tuple pattern */
    public <Z> Slots<Z> map(Function<Slot<X>,Slot<Z>> transform) {
        final int N = len();
        @SuppressWarnings("unchecked")
        Slot<Z>[] converted = (Slot<Z>[])new Object[N];
        for ( int i = 0; i < N; i++ ) {
            Slot<X> x = get(i);
            converted[i] = transform.apply(x);
        }
        return new Slots<Z>(converted);
    }

    /** Build a {@link Tuple} from the pattern with each var replaced by the "any value" */  
    public Tuple<X> replaceVar(X anyValue) { 
        // Replace Slot var with any
        final int N = len();
        @SuppressWarnings("unchecked")
        X[] converted = (X[])new Object[N];
        for ( int i = 0; i < N; i++ ) {
            Slot<X> x = get(i);
            converted[i] = (x.isVar())? anyValue : x.term;
        }
        return TupleFactory.tuple(converted);
    }
}

