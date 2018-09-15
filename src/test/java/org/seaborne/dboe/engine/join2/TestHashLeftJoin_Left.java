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

package org.seaborne.dboe.engine.join2;

import org.apache.jena.sparql.expr.ExprList;
import org.seaborne.dboe.engine.JoinKey;
import org.seaborne.dboe.engine.RowList;
import org.seaborne.dboe.engine.row.RowBuilderBase;

/** Left outer join where the left hand side used to create the hash probe table */
public class TestHashLeftJoin_Left extends AbstractTestLeftJoin {
    @Override
    public RowList<Integer> join(JoinKey joinKey, RowList<Integer> left, RowList<Integer> right, ExprList conditions) {
        return QueryIterHashLeftJoin_Left.create(joinKey, left, right, conditions, new RowBuilderBase<>());
    }
}

