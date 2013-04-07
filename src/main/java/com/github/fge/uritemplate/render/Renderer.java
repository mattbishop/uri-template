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

package com.github.fge.uritemplate.render;

import com.google.common.base.CharMatcher;
import com.google.common.primitives.UnsignedBytes;

import java.nio.charset.Charset;

public abstract class Renderer
{
    public abstract String render();

    protected static String expandString(final String s,
        final CharMatcher matcher)
    {
        final StringBuilder sb = new StringBuilder(s.length());

        for (final char c: s.toCharArray())
            sb.append(matcher.matches(c) ? c : percentEncode(c));

        return sb.toString();
    }

    private static String percentEncode(final char c)
    {
        final String tmp = new String(new char[] { c });
        final byte[] bytes = tmp.getBytes(Charset.forName("UTF-8"));
        final StringBuilder sb = new StringBuilder();
        for (final byte b: bytes)
            sb.append('%').append(UnsignedBytes.toString(b, 16));
        return sb.toString();
    }
}

