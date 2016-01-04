package com.pful.pico.db;

import com.google.gson.JsonObject;

/**
 * Created by daeyeon on 12/28/15.
 */
public class TemplateFieldOperation
		implements TemplateValueOperator<Finder.TemplateStatement>
{
	private String name;

	public TemplateFieldOperation(final String name)
	{
		this.name = name;
	}

	@Override
	public Finder.TemplateStatement is()
	{
		return is(name);
	}

	@Override
	public Finder.TemplateStatement is(final String variableName)
	{
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(name, convertNameToVariable(variableName));
		return makeField(variableName, jsonObject);
	}

	@Override
	public Finder.TemplateStatement eq()
	{
		return is();
	}

	@Override
	public Finder.TemplateStatement eq(final String variableName)
	{
		return is(variableName);
	}

	@Override
	public Finder.TemplateStatement ne()
	{
		return ne(name);
	}

	@Override
	public Finder.TemplateStatement ne(final String variableName)
	{
		return addOperationField("$ne", variableName);
	}

	@Override
	public Finder.TemplateStatement gt()
	{
		return gt(name);
	}

	@Override
	public Finder.TemplateStatement gt(final String variableName)
	{
		return addOperationField("$gt", variableName);
	}

	@Override
	public Finder.TemplateStatement gte()
	{
		return gte(name);
	}

	@Override
	public Finder.TemplateStatement gte(final String variableName)
	{
		return addOperationField("$gte", variableName);
	}

	@Override
	public Finder.TemplateStatement lt()
	{
		return lt(name);
	}

	@Override
	public Finder.TemplateStatement lt(final String variableName)
	{
		return addOperationField("$lt", variableName);
	}

	@Override
	public Finder.TemplateStatement lte()
	{
		return lte(name);
	}

	@Override
	public Finder.TemplateStatement lte(final String variableName)
	{
		return addOperationField("$lte", variableName);
	}

	@Override
	public Finder.TemplateStatement in()
	{
		return in(name);
	}

	@Override
	public Finder.TemplateStatement in(final String variableName)
	{
		return addOperationField("$in", variableName);
	}

	@Override
	public Finder.TemplateStatement nin()
	{
		return nin(name);
	}

	@Override
	public Finder.TemplateStatement nin(final String variableName)
	{
		return addOperationField("$nin", variableName);
	}

	@Override
	public Finder.TemplateStatement exists()
	{
		return exists(name);
	}

	@Override
	public Finder.TemplateStatement exists(final String variableName)
	{
		return addOperationField("$exists", variableName);
	}

	public static String convertNameToVariable(final String name)
	{
		return "<#" + name + ">";
	}

	private Finder.TemplateStatement addOperationField(final String operationName, final String variableName)
	{
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(operationName, variableName);
		return makeField(variableName, jsonObject);
	}

	private Finder.TemplateStatement makeField(final String variableName, final JsonObject jsonObject)
	{
		final JsonObject parentObject = new JsonObject();
		parentObject.add(name, jsonObject);
		return new Finder.TemplateStatement(name, variableName, jsonObject);
	}
}
