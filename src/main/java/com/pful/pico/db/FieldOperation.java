package com.pful.pico.db;


import com.google.gson.JsonArray;
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
	public Finder.Statement eq(final String value)
	{
		return is(value);
	}

	@Override
	public Finder.Statement ne(final Number value)
	{
		return new Finder.Statement(addOperationField("$ne", value));
	}

	@Override
	public Finder.Statement ne(final String value)
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
	public Finder.Statement inStringArray(final String[] strings)
	{
		return in(Util.makeArrayToJsonArrayObject(strings));
	}

	@Override
	public Finder.Statement inStrings(final String... strings)
	{
		return inStringArray(strings);
	}

	@Override
	public Finder.Statement inNumberCollection(final Collection<Number> collection)
	{
		final JsonArray jsonArray = new JsonArray();
		collection.stream().forEach(jsonArray::add);
		return in(jsonArray);
	}

	@Override
	public Finder.Statement inNumberArray(final Number[] numbers)
	{
		return in(Util.makeArrayToJsonArrayObject(numbers));
	}

	@Override
	public Finder.Statement inNumbers(final Number... numbers)
	{
		return inNumberArray(numbers);
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
	public Finder.Statement ninStringArray(final String[] strings)
	{
		return nin(Util.makeArrayToJsonArrayObject(strings));
	}

	@Override
	public Finder.Statement ninStrings(final String... strings)
	{
		return ninStringArray(strings);
	}

	@Override
	public Finder.Statement ninNumberCollection(final Collection<Number> collection)
	{
		final JsonArray jsonArray = new JsonArray();
		collection.stream().forEach(jsonArray::add);
		return nin(jsonArray);
	}

	@Override
	public Finder.Statement ninNumberArray(final Number[] numbers)
	{
		return nin(Util.makeArrayToJsonArrayObject(numbers));
	}

	@Override
	public Finder.Statement ninNumbers(final Number... numbers)
	{
		return ninNumberArray(numbers);
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

	@Override
	public Finder.Statement allInStringArray(final String[] strings)
	{
		return all(Util.makeArrayToJsonArrayObject(strings));
	}

	@Override
	public Finder.Statement allInStrings(final String... strings)
	{
		return all(Util.makeArrayToJsonArrayObject(strings));
	}

	@Override
	public Finder.Statement allInStringCollection(final Collection<String> collection)
	{
		return all(Util.makeStringCollectionToJsonArrayObject(collection));
	}

	@Override
	public Finder.Statement allInNumberArray(final Number[] numbers)
	{
		return all(Util.makeArrayToJsonArrayObject(numbers));
	}

	@Override
	public Finder.Statement allInNumbers(final Number... numbers)
	{
		return all(Util.makeArrayToJsonArrayObject(numbers));
	}

	@Override
	public Finder.Statement allInNumberCollection(final Collection<Number> collection)
	{
		return all(Util.makeNumberCollectionToJsonArrayObject(collection));
	}

	private Finder.Statement all(final JsonArray conditions)
	{
		final JsonObject jsonObject = new JsonObject();
		jsonObject.add("$all", conditions);

		return new Finder.Statement(makeField(jsonObject));
	}


	private JsonObject addOperationField(final String operationName, final Number value)
	{
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(operationName, value);
		return makeField(jsonObject);
	}

	private JsonObject addOperationField(final String operationName, final String value)
	{
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(operationName, value);
		return makeField(jsonObject);
	}


	private JsonObject makeField(final JsonObject jsonElement)
	{
		final JsonObject jsonObject = new JsonObject();
		jsonObject.add(name, jsonElement);
		return jsonObject;
	}
}