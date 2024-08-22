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

package org.seaborne.dboe.engine.join;

import java.util.*;

import org.apache.jena.atlas.iterator.Iter;
import org.apache.jena.atlas.lib.SetUtils;
import org.apache.jena.atlas.logging.FmtLog;
import org.apache.jena.sparql.core.Var;
import org.seaborne.dboe.engine.*;
import org.apache.commons.collections4.MultiMapUtils;
import org.apache.commons.collections4.MultiValuedMap;

/** Implementation of HashJoin that materializes its results and then returns an iterator.
 *  As much a test of the algorithm.
 */
public class HashJoinConcrete {
    /** Evaluate and materialize.  This serves to provide a check of the algorithm. */
    // This code predates the RHS-streaming version, otherwise we could have
    // used that by inheritance simply materializing the RHS
    // then passing RowList to the parent.
    public static <X> RowList<X> hashJoinConcrete(JoinKey joinKey, RowList<X> left, RowList<X> right, RowBuilder<X> builder) {
        Hasher<X> hasher = JL.hash();
        return hashJoinConcrete(joinKey, left, right, hasher, builder);
    }

    /** HashJoin that materializes the results */
    public static <X> RowList<X> hashJoinConcrete(JoinKey joinKey, RowList<X> left, RowList<X> right,
                                                  Hasher<X> hasher , RowBuilder<X> builder) {
        // Assume left smaller than right so hash left
        Set<Var> vars = SetUtils.union(left.vars(), right.vars());
        if ( Quack.JOIN_EXPLAIN ) FmtLog.info(Quack.joinStatsLog, "Phase 1 : "+joinKey);
        // Phase 1.hasher,
        MultiValuedMap<Object, Row<X>> buckets = MultiMapUtils.newListValuedHashMap();
        long count1 = 0;
        Iterator<Row<X>> iter1 = left.iterator();
        for (; iter1.hasNext();) {
            Row<X> row1 = iter1.next();
            count1++;
            Object longHash = JL.hash(hasher, joinKey, row1);
            if ( Quack.JOIN_EXPLAIN ) FmtLog.info(Quack.joinStatsLog, "Hash = 0x%08Xd", longHash);
            buckets.put(longHash, row1);
        }
        iter1 = null;
        if ( Quack.JOIN_EXPLAIN ) FmtLog.info(Quack.joinStatsLog, "Phase 1 - LHS=%d", count1);
        // Phase 2
        if ( Quack.JOIN_EXPLAIN ) FmtLog.info(Quack.joinStatsLog, "Phase 2");
        long count2 = 0;
        List<Row<X>> results = new ArrayList<>();

        // Left table, no overlap.
        Collection<Row<X>> leftNoKey = buckets.get(JL.noKeyHash);

        Iterator<Row<X>> iter2 = right.iterator();
        for (; iter2.hasNext();) {
            Row<X> row2 = iter2.next();
            count2 ++;
            Object longHash = JL.hash(hasher, joinKey, row2);
            if ( Quack.JOIN_EXPLAIN ) FmtLog.info(Quack.joinStatsLog, "Hash = 0x%04X", longHash);

            if ( longHash == JL.noKeyHash ) {
                // No key on the right.
                // Need to cross product with the left.
                Iterator<Row<X>> iter = buckets.values().iterator();
                for (; iter.hasNext(); ) {
                    Row<X> rLeft = iter.next();
                    builder.reset();
                    Row<X> r = Join.merge(rLeft, row2, builder);
                    if (r != null) {
                        results.add(r);
                    }  // else { LeftJoin } XXX
                }
                continue;
            }

            Iterator<Row<X>> iter;
            if ( longHash == JL.noKeyHash ) {
                // No shared vars with the JoinKey; iterator over the whole of the left.
                iter = buckets.values().iterator();
            } else {
                // Get all rows matching with the hash from the right row.
                Collection<Row<X>> sameKey = buckets.get(longHash);    // Maybe null.
                iter = ( sameKey == null ) ? null : sameKey.iterator();
                if ( leftNoKey != null )
                    iter = Iter.concat(iter, leftNoKey.iterator());
            }

            if ( iter != null ) {
                for (; iter.hasNext(); ) {
                    Row<X> rLeft = iter.next();
                    builder.reset();
                    Row<X> r = Join.merge(rLeft, row2, builder);
                    if (r != null)
                        results.add(r);
                }
            }
        }

        long count3 = results.size();
        if ( Quack.JOIN_EXPLAIN ) FmtLog.info(Quack.joinStatsLog, "%d : %d : %d", count1, count2, count3);

        return RowLib.createRowList(vars, results.iterator());
    }

}

