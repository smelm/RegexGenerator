/*
 * Copyright (C) 2015 Machine Learning Lab - University of Trieste, 
 * Italy (http://machinelearning.inginf.units.it/)  
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.units.inginf.male.tree;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.*;

/**
 *
 * @author MaleLabTs
 */
public class RegexRange extends AbstractNode implements Leaf {

    private Node parent;
    protected String value;

    public RegexRange(String value) {
        this.value = value;
    }

    @Override
    public int getMinChildrenCount() {
        return 0;
    }

    @Override
    public int getMaxChildrenCount() {
        return 0;
    }

    @Override
    public void describe(StringBuilder builder, DescriptionContext context, RegexFlavour flavour) {
        builder.append(value);
    }

    @Override
    public Leaf cloneTree() {
        RegexRange clone = new RegexRange(value);
        return clone;
    }

    @Override
    public Node getParent() {
        return parent;
    }

    @Override
    public void setParent(Node parent) {
        this.parent = parent;
    }

    @Override
    public List<Node> getChildrens() {
        return Collections.<Node>emptyList();
    }

    /**
     * Valido solo se sono all'interno di ListMatch o ListNotMatch
     * @return
     */
    @Override
    public boolean isValid() {
        return false;
    }


    @Override
    public boolean isCharacterClass() {
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RegexRange other = (RegexRange) obj;
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }

    private void addElementToRangeList(JsonArray arr, String a){
        addElementToRangeList(arr, a, a);
    }

    private void addElementToRangeList(JsonArray arr, String a, String b){
        var tuple = new JsonArray();
        tuple.add(new JsonPrimitive(a));
        tuple.add(new JsonPrimitive(b));
        arr.add(tuple);
    }

    @Override
    public JsonObject toJson() {
        // TODO this does not work with classes not containing
        var rangeList = new JsonArray();

        var splits = value.split("-");

        var split = splits[0];

        for(int i = 0; i < split.length() - 1; i++){
            addElementToRangeList(rangeList, split.substring(i, i+1));
        }

        String lower = split.substring(split.length() - 1, split.length());

        for(int i= 1; i < splits.length; i++){
            split = splits[i];
            String upper = split.substring(0, 1);
            addElementToRangeList(rangeList, lower, upper);

            for(int j = 1; j < split.length() - 1; j++){
                addElementToRangeList(rangeList, split.substring(j + 1));
            }
            lower = split.substring(split.length() - 1, split.length());
        }

        if(split.length() > 1){
            addElementToRangeList(rangeList, lower);
        }

        var obj = new JsonObject();
        obj.addProperty("type", "characterClass");
        obj.add("members", rangeList);
        return obj;
    }
}
