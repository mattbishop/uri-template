/*
 * Copyright (c) 2013, Francis Galiegue <fgaliegue@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Lesser GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.fge.uritemplate;

import com.beust.jcommander.internal.Lists;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.uritemplate.vars.values.ListValue;
import com.github.fge.uritemplate.vars.values.MapValue;
import com.github.fge.uritemplate.vars.values.ScalarValue;
import com.github.fge.uritemplate.vars.values.VariableValue;
import com.google.common.collect.Maps;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public final class ExtendedTestsTest
{
    private JsonNode data;

    @BeforeClass
    public void initData()
        throws IOException
    {
        final String resourceName = "/extended-tests.json";
        data = new ObjectMapper()
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
            .readTree(ExtendedTestsTest.class.getResourceAsStream(resourceName));
    }

    @DataProvider
    public Iterator<Object[]> getData()
    {
        final List<Object[]> list = Lists.newArrayList();

        Map<String, VariableValue> vars;
        Iterator<Map.Entry<String, JsonNode>> iterator;
        Map.Entry<String, JsonNode> entry;

        for (final JsonNode node: data) { // Cycle through values
            if (node.path("disabled").asBoolean(false))
                continue;
            vars = Maps.newHashMap();
            iterator = node.get("variables").fields();
            while (iterator.hasNext()) {
                entry = iterator.next();
                if (entry.getValue().isNull())
                    continue;
                vars.put(entry.getKey(), valueOf(entry.getValue()));
            }
            for (final JsonNode n: node.get("testcases"))
                list.add(new Object[]{ n.get(0).textValue(), vars, n.get(1) });
        }

        Collections.shuffle(list);

        return list.iterator();
    }

    @Test(dataProvider = "getData")
    public void illegalTemplatesAreMarkedAsSuch(final String tmpl,
        final Map<String, VariableValue> vars, final JsonNode resultNode)
        throws URITemplateException
    {
        final URITemplate template = new URITemplate(tmpl);
        final String actual = template.expand(vars);

        if (resultNode.isTextual()) {
            assertEquals(actual, resultNode.textValue());
            return;
        }

        if (!resultNode.isArray())
            throw new RuntimeException("Didn't expect that");

        boolean found = false;
        for (final JsonNode node: resultNode)
            if (node.textValue().equals(actual))
                found = true;

        assertTrue(found, "no value matched expansion");
    }

    private static VariableValue valueOf(final JsonNode node)
    {
        if (node.isTextual())
            return new ScalarValue(node.textValue());
        if (node.isArray()) {
            final List<String> list = Lists.newArrayList();
            for (final JsonNode element: node)
                list.add(element.textValue());
            return new ListValue(list);
        }
        if (node.isObject()) {
            final Map<String, String> map = Maps.newHashMap();
            final Iterator<Map.Entry<String, JsonNode>> iterator
                = node.fields();
            Map.Entry<String, JsonNode> entry;
            while (iterator.hasNext()) {
                entry = iterator.next();
                map.put(entry.getKey(), entry.getValue().textValue());
            }
            return new MapValue(map);
        }
        throw new RuntimeException("cannot bind JSON to variable value");
    }
}

