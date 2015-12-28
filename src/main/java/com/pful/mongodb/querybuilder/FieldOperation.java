package com.pful.mongodb.querybuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Collection;

public class FieldOperation
		implements ValueOperator<Finder.Statement>
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
	public Finder.Statement inStringCollection(final Collection<String> collection)
	{
		final JsonArray jsonArray = new JsonArray();
		collection.stream().forEach(jsonArray::add);
		return in(jsonArray);
	}

	@Override
	public Finder.Statement inNumberCollection(final Collection<Number> collection)
	{
		final JsonArray jsonArray = new JsonArray();
		collection.stream().forEach(jsonArray::add);
		return in(jsonArray);
	}

	private Finder.Statement in(final JsonArray jsonArray)
	{
		final JsonObject jsonObject = new JsonObject();
		jsonObject.add("$in", jsonArray);
		return new Finder.Statement(makeField(jsonObject));
	}

	@Override
	public Finder.Statement ninStringCollection(final Collection<String> collection)
	{
		final JsonArray jsonArray = new JsonArray();
		collection.stream().forEach(jsonArray::add);
		return nin(jsonArray);
	}

	@Override
	public Finder.Statement ninNumberCollection(final Collection<Number> collection)
	{
		final JsonArray jsonArray = new JsonArray();
		collection.stream().forEach(jsonArray::add);
		return nin(jsonArray);
	}

	private Finder.Statement nin(final JsonArray jsonArray)
	{
		final JsonObject jsonObject = new JsonObject();
		jsonObject.add("$nin", jsonArray);
		return new Finder.Statement(makeField(jsonObject));
	}

	@Override
	public Finder.Statement exists()
	{
		final JsonObject existsObject = new JsonObject();
		existsObject.addProperty("$exists", true);

		final JsonObject jsonObject = new JsonObject();
		jsonObject.add(name, existsObject);

		return new Finder.Statement(makeField(jsonObject));
	}

	private JsonObject addOperationField(final String operationName, final Number value)
	{
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(operationName, value);
		return makeField(jsonObject);
	}

	private JsonObject makeField(final JsonElement jsonElement)
	{
		final JsonObject jsonObject = new JsonObject();
		jsonObject.add(name, jsonElement);
		return jsonObject;
	}
}