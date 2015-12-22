package com.pful.mongodb.querybuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Finder
{
	private JsonParser jsonParser = new JsonParser();

	private String collectionName;
	private Expression root = new Expression();
	private List<JsonObject> conditionList = new ArrayList<>();

	public Finder(final String collectionName)
	{
		this.collectionName = collectionName;
	}

	public static Expression inCollection(final String name)
	{
		final Finder finder = new Finder(name);
		return finder.root;
	}

	private JsonObject makeJson()
	{
		final JsonObject rootNode = new JsonObject();

		for (final JsonObject jsonElement : conditionList) {
			for (final Map.Entry<String, JsonElement> entry : jsonElement.entrySet()) {
				rootNode.add(entry.getKey(), entry.getValue());
			}
		}

		return rootNode;
	}

	public interface Query
	{
		JsonElement toJson();
	}

	public interface ValueOperator<TReturn>
	{
		TReturn is(final String value);

		TReturn is(final Number value);

		TReturn eq(final Number value);

		TReturn ne(final Number value);

		TReturn gt(final Number value);

		TReturn gte(final Number value);

		TReturn lt(final Number value);

		TReturn lte(final Number value);

		TReturn in(final Collection<String> elements);

		TReturn nin(final Collection<String> elements);

		TReturn exists(final String name);
	}

	public static class Statement
			implements Query
	{
		final JsonElement jsonElement;

		public Statement(final JsonElement jsonElement)
		{
			this.jsonElement = jsonElement;
		}

		public JsonElement toJson()
		{
			return jsonElement;
		}
	}

	public class Expression
			implements Query
	{
		public Expression allOf(final Query... quries)
		{
			final JsonObject subDoc = new JsonObject();
			subDoc.add("$and", makeJsonArray(quries));
			conditionList.add(subDoc);
			return this;
		}

		private JsonArray makeJsonArray(final Query[] queries)
		{
			final JsonArray jsonArray = new JsonArray();

			for (final Query query : queries) {
				jsonArray.add(query.toJson());
			}

			return jsonArray;
		}

		public Expression anyOf(final Query... quries)
		{
			final JsonObject subDoc = new JsonObject();
			subDoc.add("$or", makeJsonArray(quries));
			conditionList.add(subDoc);
			return this;
		}

		public Expression noneOf(final Query... quries)
		{
			final JsonObject subDoc = new JsonObject();
			subDoc.add("$nor", makeJsonArray(quries));
			conditionList.add(subDoc);
			return this;
		}

		public ExpressionOperation field(final String name)
		{
			return new ExpressionOperation(name, this);
		}

		public JsonElement toJson()
		{
			return makeJson();
		}
	}

	public class ExpressionOperation
			implements ValueOperator<Expression>
	{
		private String name;
		private Expression parentExpression;

		public ExpressionOperation(final String name, final Expression parentExpression)
		{
			this.name = name;
			this.parentExpression = parentExpression;
		}

		@Override
		public Expression is(final String value)
		{
			final JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty(name, value);
			conditionList.add(jsonObject);
			return this.parentExpression;
		}

		@Override
		public Expression is(final Number value)
		{
			final JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty(name, value);
			conditionList.add(jsonObject);
			return this.parentExpression;
		}

		@Override
		public Expression eq(final Number value)
		{
			return is(value);
		}

		private void addOperationField(final String operationName, final Number value)
		{
			final JsonObject operationNode = new JsonObject();
			operationNode.addProperty(operationName, value);

			makeField(operationNode);
		}

		private void makeField(final JsonElement jsonElement)
		{
			final JsonObject node = new JsonObject();
			node.add(name, jsonElement);

			conditionList.add(node);
		}

		@Override
		public Expression ne(final Number value)
		{
			addOperationField("$ne", value);
			return parentExpression;
		}

		@Override
		public Expression gt(final Number value)
		{
			addOperationField("$gt", value);
			return parentExpression;
		}

		@Override
		public Expression gte(final Number value)
		{
			addOperationField("$gte", value);
			return parentExpression;
		}

		@Override
		public Expression lt(final Number value)
		{
			addOperationField("lt", value);
			return parentExpression;
		}

		@Override
		public Expression lte(final Number value)
		{
			addOperationField("lte", value);
			return parentExpression;
		}

		@Override
		public Expression in(final Collection<String> elements)
		{
			final JsonArray elementArray = new JsonArray();
			for (final String element : elements) {
				elementArray.add(element);
			}

			final JsonObject operationNode = new JsonObject();
			operationNode.add("$in", elementArray);

			makeField(operationNode);
			return parentExpression;
		}

		@Override
		public Expression nin(final Collection<String> elements)
		{
			final JsonArray elementArray = new JsonArray();
			for (final String element : elements) {
				elementArray.add(element);
			}

			final JsonObject operationNode = new JsonObject();
			operationNode.add("$nin", elementArray);

			makeField(operationNode);
			return parentExpression;
		}

		@Override
		public Expression exists(final String name)
		{
			final JsonObject operationNode = new JsonObject();
			operationNode.addProperty("$exists", name);

			makeField(operationNode);
			return parentExpression;
		}
	}

}