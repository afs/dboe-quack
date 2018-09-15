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

//import static org.junit.Assert.*;
import org.apache.jena.sparql.core.Var;
//import org.junit.Test;

public class T_TestSlots
{
    static Var var = Var.alloc("v");  
    
//    @Test public void slots_01() {
//        Slots<String> slots = Slots.create();
//        assertEquals(0, slots.size());
//    } 
//    
//    @Test public void slots_02() {
//        Slot<String> x = Slot.createTermSlot("X");
//        Slot<String> v = Slot.createVarSlot(var);
//        Slots<String> slots = Slots.create(x,v);
//        assertEquals(2, slots.size());
//        assertEquals(x, slots.get(0));
//        assertNotEquals(var, slots.get(0));
//        assertNotEquals(v, slots.get(0));
//    }
//    
//    @Test public void slots_03() {
//        Slot<String> x = Slot.createTermSlot("X");
//        Slot<String> v = Slot.createVarSlot(var);
//        Slots<String> slots = Slots.create(x,v);
//        Tuple<String> tuple = slots.replaceVar("ANY");
//        assertEquals(2, tuple.size());
//        assertEquals("X", tuple.get(0));
//        assertEquals("ANY", tuple.get(1));
//    }
    
}
