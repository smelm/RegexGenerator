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
package it.units.inginf.male.tree.operator;

import com.google.gson.JsonObject;
import it.units.inginf.male.tree.Constant;
import it.units.inginf.male.tree.DescriptionContext;
import it.units.inginf.male.tree.Node;
import it.units.inginf.male.tree.RegexRange;

/**
 *
 * @author MaleLabTs
 */
public class ListNotMatch extends UnaryOperator {
    
    @Override
    protected UnaryOperator buildCopy() {
        return new ListNotMatch();
    }

    @Override
    public void describe(StringBuilder builder, DescriptionContext context, RegexFlavour flavour) {
        builder.append("[^");
        Node child = getChildrens().get(0);
        child.describe(builder, context, flavour);
        builder.append("]");
    }
    
    @Override
    public boolean isValid() {
        return checkValid(getChildrens().get(0));
    }

    private boolean checkValid(Node root){

        if(!(root instanceof Constant || root instanceof RegexRange || root instanceof Concatenator)){
            return false;
        }

        for(Node child:root.getChildrens()){
            if(!checkValid(child))
                return false;
        }

        return true;
    }  
    
    @Override
    public boolean isCharacterClass() {
        return true;
    }

    @Override
    public JsonObject toJson() {
        if(!isValid()){
            throw new IllegalStateException("invalid character class");
        }

        var obj = new JsonObject();
        obj.addProperty("type", "characterClass");
        var children = ListMatch.prepareChildren(getChildrens().get(0));
        obj.add("children", children);
        obj.addProperty("inverted", true);
        return obj;

    }
}
