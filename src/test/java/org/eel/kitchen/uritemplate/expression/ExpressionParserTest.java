/*
 * Copyright (c) 2012, Francis Galiegue <fgaliegue@gmail.com>
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

package org.eel.kitchen.uritemplate.expression;

import com.google.common.collect.ImmutableSet;
import org.eel.kitchen.uritemplate.InvalidTemplateException;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public final class ExpressionParserTest
{
    @Test
    public void legalSingleVariableReferenceIsParsedCorrectly()
        throws InvalidTemplateException
    {
        final String input = "foo";
        final Expression expression = ExpressionParser.parse(input);

        assertEquals(expression.getVarNames(), ImmutableSet.of(input));
    }

    // As the RFC requires at least one character in a variable name, this means
    // empty inputs are not accepted
    @Test
    public void emptySingleVariableReferenceIsIllegal()
    {
        try {
            ExpressionParser.parse("");
            fail("No exception thrown!");
        } catch (InvalidTemplateException e) {
            assertEquals(e.getMessage(), "variable names cannot be empty");
        }
    }

    @Test
    public void multipleVariableReferencesAreParsedCorrectly()
        throws InvalidTemplateException
    {
        final String input = "foo,bar";
        final Expression expression = ExpressionParser.parse(input);

        assertEquals(expression.getVarNames(), ImmutableSet.of("foo", "bar"));
    }
}