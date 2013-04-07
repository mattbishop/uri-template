package com.github.fge.uritemplate.expression;

import com.github.fge.uritemplate.URITemplateException;
import com.github.fge.uritemplate.vars.specs.VariableSpec;
import com.github.fge.uritemplate.vars.values.VariableValue;

import java.util.List;
import java.util.Map;

public interface URITemplateExpression
{
    String expand(final Map<String, VariableValue> vars)
        throws URITemplateException;

    String getLiteral();

    ExpressionType getExpressionType();

    List<VariableSpec> getVariableSpecs();
}
