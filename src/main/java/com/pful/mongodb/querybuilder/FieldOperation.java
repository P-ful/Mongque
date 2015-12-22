package com.pful.mongodb.querybuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Collection;

public class FieldOperation
		implements Finder.ValueOperator<Finder.Statement>
{

	private String name;

	public FieldOperation(final String name)
	{
		this.name = name;
	}

	@Override
	public Finder.Statement is(final String value)
	{
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(name, value);

		return new Finder.Statement(jsonObject);
	}

	@Override
	public Finder.Statement is(final Number value)
	{
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(name, value);

		return new Finder.Statement(jsonObject);
	}

	@Override
	public Finder.Statement eq(final Number value)
	{
		return is(value);
	}

	private JsonObject addOperationField(final String operationName, final Number value)
	{
		final JsonObject operationNode = new JsonObject();
		operationNode.addProperty(operationName, value);

		return makeField(operationNode);
	}

	private JsonObject makeField(final JsonElement jsonElement)
	{
		final JsonObject node = new JsonObject();
		node.add(name, jsonElement);

		return node;
	}

	@Override
	public Finder.Statement ne(final Number value)
	{
		return new Finder.Statement(addOperationField("$ne", value));
	}

	@Override
	public Finder.Statement gt(final Number value)
	{
		return new Finder.Statement(addOperationField("$gt", value));
	}

	@Override
	public Finder.Statement gte(final Number value)
	{
		return new Finder.Statement(addOperationField("$gte", value));
	}

	@Override
	public Finder.Statement lt(final Number value)
	{
		return new Finder.Statement(addOperationField("$lt", value));
	}

	@Override
	public Finder.Statement lte(final Number value)
	{
		return new Finder.Statement(addOperationField("$lte", value));
	}

	@Override
	public Finder.Statement in(final Collection<String> elements)
	{
		final JsonArray elementArray = new JsonArray();
		for (final String element : elements) {
			elementArray.add(element);
		}

		final JsonObject operationNode = new JsonObject();
		operationNode.add("$in", elementArray);

		return new Finder.Statement(makeField(operationNode));
	}

	@Override
	public Finder.Statement nin(final Collection<String> elements)
	{
		final JsonArray elementArray = new JsonArray();
		for (final String element : elements) {
			elementArray.add(element);
		}

		final JsonObject operationNode = new JsonObject();
		operationNode.add("$nin", elementArray);

		return new Finder.Statement(makeField(operationNode));
	}

	@Override
	public Finder.Statement exists(final String name)
	{
		final JsonObject operationNode = new JsonObject();
		operationNode.addProperty("$exists", name);

		return new Finder.Statement(makeField(operationNode));
	}
}