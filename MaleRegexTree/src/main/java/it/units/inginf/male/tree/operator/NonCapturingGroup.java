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
import it.units.inginf.male.tree.DescriptionContext;

/**
 *
 * @author MaleLabTs
 */
public class NonCapturingGroup extends UnaryOperator {

    @Override
    protected UnaryOperator buildCopy() {
        return new NonCapturingGroup();
    }

    @Override
    public void describe(StringBuilder builder, DescriptionContext context, RegexFlavour flavour) {
        builder.append("(?:");
        getChildrens().get(0).describe(builder, context, flavour);
        builder.append(")");
    }

    @Override
    public boolean isValid() {
        return getChildrens().get(0).isValid();
    }

    @Override
    public JsonObject toJson() {
        var obj = new JsonObject();
        obj.addProperty("type", "group");
        obj.add("child", getChildrens().get(0).toJson());
        obj.addProperty("name", "xyz"); //TODO
        return obj;
    }
}
