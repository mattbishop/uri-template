package com.github.fge.uritemplate.expression;

import com.github.fge.uritemplate.vars.specs.VariableSpec;
import com.github.fge.uritemplate.vars.values.VariableValue;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Map;

public final class TemplateLiteral
    implements URITemplateExpression
{
    private final String literal;

    public TemplateLiteral(final String literal)
    {
        this.literal = literal;
    }

    @Override
    public String expand(final Map<String, VariableValue> vars)
    {
        return literal;
    }

    @Override
    public String getLiteral()
    {
        return literal;
    }

    @Override
    public ExpressionType getExpressionType()
    {
        return null;
    }

    @Override
    public List<VariableSpec> getVariableSpecs()
    {
        return ImmutableList.of();
    }
}
