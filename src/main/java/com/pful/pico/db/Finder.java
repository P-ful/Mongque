package com.pful.pico.db;

import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;

public class Finder
{
	private static Map<String, TemplateBuilder> templates = new HashMap<>();

	private Expression root = new Expression();
	private List<JsonObject> conditionList = new ArrayList<>();

	public static TemplateBuilder.TemplateExpression registerTemplate(final String alias)
	{
		final Finder finder = new Finder();
		final TemplateBuilder builder = finder.new TemplateBuilder(finder);
		templates.put(alias, builder);
		return builder.root;
	}

	public static TemplateBuilder.VariableBinder openQuery(final String alias)
			throws QueryBuilderException
	{
		checkArgument(!alias.isEmpty());

		if (!templates.containsKey(alias)) {
			throw new QueryBuilderException("No exists such query.");
		}

		final TemplateBuilder builder = templates.get(alias);
		return builder.new VariableBinder();
	}


	public static Expression newQuery()
	{
		final Finder finder = new Finder();
		return finder.root;
	}

	private JsonObject makeJson()
	{
		final JsonObject rootNode = new JsonObject();

		conditionList.stream().forEach(
				e -> e.entrySet().forEach(
						entry -> rootNode.add(entry.getKey(), entry.getValue())));

		return rootNode;
	}

	public interface Query
	{
		JsonObject toJson();
	}

	public static class TemplateStatement
			extends Statement
	{
		private String fieldName;
		private String variableName;

		public TemplateStatement(final String fieldName, final String variableName, final JsonObject jsonElement)
		{
			super(jsonElement);
			this.fieldName = fieldName;
			this.variableName = variableName;
		}
	}

	public static class Statement
			implements Query
	{
		final JsonObject jsonObject;

		public Statement(final JsonObject jsonObject)
		{
			this.jsonObject = jsonObject;
		}

		public JsonObject toJson()
		{
			return jsonObject;
		}
	}

	public class Expression
			implements Query
	{
		public Expression allOf(final Query... queries)
		{
			final JsonObject jsonObject = new JsonObject();
			jsonObject.add("$and", makeJsonArray(queries));
			conditionList.add(jsonObject);
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

		public Expression anyOf(final Query... queries)
		{
			final JsonObject jsonObject = new JsonObject();
			jsonObject.add("$or", makeJsonArray(queries));
			conditionList.add(jsonObject);
			return this;
		}

		public Expression noneOf(final Query... queries)
		{
			final JsonObject jsonObject = new JsonObject();
			jsonObject.add("$nor", makeJsonArray(queries));
			conditionList.add(jsonObject);
			return this;
		}

		public ExpressionOperation field(final String name)
		{
			return new ExpressionOperation(name, this);
		}

		public JsonObject toJson()
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
			return parentExpression;
		}

		@Override
		public Expression is(final Number value)
		{
			final JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty(name, value);
			conditionList.add(jsonObject);
			return parentExpression;
		}

		@Override
		public Expression eq(final Number value)
		{
			return is(value);
		}

		@Override
		public Expression eq(final String value)
		{
			return is(value);
		}

		@Override
		public Expression ne(final Number value)
		{
			return addOperationField("$ne", value);
		}

		@Override
		public Expression ne(final String value)
		{
			return addOperationField("$ne", value);
		}

		@Override
		public Expression gt(final Number value)
		{
			return addOperationField("$gt", value);
		}

		@Override
		public Expression gte(final Number value)
		{
			return addOperationField("$gte", value);
		}

		@Override
		public Expression lt(final Number value)
		{
			return addOperationField("$lt", value);
		}

		@Override
		public Expression lte(final Number value)
		{
			return addOperationField("$lte", value);
		}

		@Override
		public Expression inStringCollection(final Collection<String> collection)
		{
			final JsonArray jsonArray = new JsonArray();
			collection.stream().forEach(jsonArray::add);
			return in(jsonArray);
		}

		@Override
		public Expression inStringArray(final String[] strings)
		{
			return in(Util.makeArrayToJsonArrayObject(strings));
		}

		@Override
		public Expression inStrings(final String... strings)
		{
			return inStringArray(strings);
		}

		@Override
		public Expression inNumberCollection(final Collection<Number> collection)
		{
			final JsonArray jsonArray = new JsonArray();
			collection.stream().forEach(jsonArray::add);
			return in(jsonArray);
		}

		@Override
		public Expression inNumberArray(final Number[] numbers)
		{
			return in(Util.makeArrayToJsonArrayObject(numbers));
		}

		@Override
		public Expression inNumbers(final Number... numbers)
		{
			return inNumberArray(numbers);
		}

		private Expression in(final JsonArray jsonArray)
		{
			final JsonObject operationNode = new JsonObject();
			operationNode.add("$in", jsonArray);

			return makeField(operationNode);
		}

		@Override
		public Expression ninStringCollection(final Collection<String> collection)
		{
			final JsonArray jsonArray = new JsonArray();
			collection.stream().forEach(jsonArray::add);
			return nin(jsonArray);
		}

		@Override
		public Expression ninStringArray(final String[] strings)
		{
			return nin(Util.makeArrayToJsonArrayObject(strings));
		}

		@Override
		public Expression ninStrings(final String... strings)
		{
			return ninStringArray(strings);
		}

		@Override
		public Expression ninNumberCollection(final Collection<Number> collection)
		{
			final JsonArray jsonArray = new JsonArray();
			collection.stream().forEach(jsonArray::add);
			return nin(jsonArray);
		}

		@Override
		public Expression ninNumberArray(final Number[] numbers)
		{
			return nin(Util.makeArrayToJsonArrayObject(numbers));
		}

		@Override
		public Expression ninNumbers(final Number... numbers)
		{
			return ninNumberArray(numbers);
		}

		private Expression nin(final JsonArray jsonArray)
		{
			final JsonObject jsonObject = new JsonObject();
			jsonObject.add("$nin", jsonArray);

			return makeField(jsonObject);
		}

		@Override
		public Expression exists()
		{
			final JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("$exists", true);
			return makeField(jsonObject);
		}

		@Override
		public Expression allInStringArray(final String[] strings)
		{
			return all(Util.makeArrayToJsonArrayObject(strings));
		}

		@Override
		public Expression allInStrings(final String... strings)
		{
			return all(Util.makeArrayToJsonArrayObject(strings));
		}

		@Override
		public Expression allInStringCollection(final Collection<String> collection)
		{
			return all(Util.makeStringCollectionToJsonArrayObject(collection));
		}

		@Override
		public Expression allInNumberArray(final Number[] numbers)
		{
			return all(Util.makeArrayToJsonArrayObject(numbers));
		}

		@Override
		public Expression allInNumbers(final Number... numbers)
		{
			return all(Util.makeArrayToJsonArrayObject(numbers));
		}

		@Override
		public Expression allInNumberCollection(final Collection<Number> collection)
		{
			return all(Util.makeNumberCollectionToJsonArrayObject(collection));
		}

		private Expression all(final JsonArray conditions)
		{
			final JsonObject jsonObject = new JsonObject();
			jsonObject.add("$all", conditions);

			return makeField(jsonObject);
		}

		private Expression addOperationField(final String operationName, final Number value)
		{
			final JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty(operationName, value);

			return makeField(jsonObject);
		}

		private Expression addOperationField(final String operationName, final String value)
		{
			final JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty(operationName, value);

			return makeField(jsonObject);
		}


		private Expression makeField(final JsonObject jsonElement)
		{
			final JsonObject jsonObject = new JsonObject();
			jsonObject.add(name, jsonElement);

			conditionList.add(jsonObject);
			return parentExpression;
		}
	}

	/**
	 * Created by daeyeon on 12/28/15.
	 */
	public class TemplateBuilder
	{
		private final Multimap<String, TemplateVariable> variables = ArrayListMultimap.create();
		private Finder finder;
		private TemplateExpression root = new TemplateExpression();

		public TemplateBuilder(final Finder finder)
		{
			this.finder = finder;
		}

		private class TemplateVariable
		{
			public String fieldName;
			public JsonObject jsonObject;

			public TemplateVariable(final String name, final JsonObject jsonObject)
			{
				this.fieldName = name;
				this.jsonObject = jsonObject;
			}

			public void transform(final String variableName, final String value)
			{
				jsonObject.addProperty(fieldName, value);
			}

			public void transform(final String variableName, final Number value)
			{
				jsonObject.addProperty(fieldName, value);
			}

			public void transform(final String variableName, final JsonArray jsonArray)
			{
				jsonObject.add(fieldName, jsonArray);
			}
		}

		private class ExistsTemplateVariable
				extends TemplateVariable
		{
			public ExistsTemplateVariable(final String name, final JsonObject jsonObject)
			{
				super(name, jsonObject);
			}

			@Override
			public void transform(final String variableName, final String value)
			{
				jsonObject.remove(fieldName);

				final JsonObject existsObject = new JsonObject();
				existsObject.addProperty("$exists", true);

				jsonObject.add(value, existsObject);
			}

			@Override
			public void transform(final String variableName, final Number value)
			{
				System.out.println(variableName);
			}

			@Override
			public void transform(final String variableName, final JsonArray jsonArray)
			{
				System.out.println(variableName);
			}
		}

		public class TemplateExpression
				extends Expression
		{
			@Override
			public TemplateExpression allOf(final Query... queries)
			{
				for (final Query query : queries) {
					if (query instanceof TemplateStatement) {
						final TemplateStatement statement = (TemplateStatement) query;
						variables.put(statement.fieldName, new TemplateVariable(statement.variableName, statement.jsonObject));
					}
				}

				return (TemplateExpression) super.allOf(queries);
			}

			@Override
			public TemplateExpression anyOf(final Query... queries)
			{
				for (final Query query : queries) {
					if (query instanceof TemplateStatement) {
						final TemplateStatement statement = (TemplateStatement) query;
						variables.put(statement.fieldName, new TemplateVariable(statement.variableName, statement.jsonObject));
					}
				}

				return (TemplateExpression) super.anyOf(queries);
			}

			@Override
			public TemplateExpression noneOf(final Query... queries)
			{
				for (final Query query : queries) {
					if (query instanceof TemplateStatement) {
						final TemplateStatement statement = (TemplateStatement) query;
						variables.put(statement.fieldName, new TemplateVariable(statement.variableName, statement.jsonObject));
					}
				}

				return (TemplateExpression) super.noneOf(queries);
			}

			public TemplateExpressionOperation templateField(final String name)
			{
				return new TemplateExpressionOperation(name, this);
			}

		}

		public class TemplateExpressionOperation
				implements TemplateValueOperator<TemplateExpression>
		{
			private String name;
			private TemplateExpression parentExpression;

			public TemplateExpressionOperation(final String name, final TemplateExpression parentExpression)
			{
				this.name = name;
				this.parentExpression = parentExpression;
			}

			@Override
			public TemplateExpression is()
			{
				return is(name);
			}

			@Override
			public TemplateExpression is(final String variableName)
			{
				final JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty(name, TemplateFieldOperation.convertNameToVariable(variableName));

				finder.conditionList.add(jsonObject);
				variables.put(variableName, new TemplateVariable(name, jsonObject));
				return parentExpression;
			}

			@Override
			public TemplateExpression eq()
			{
				return is();
			}

			@Override
			public TemplateExpression eq(final String variableName)
			{
				return is(variableName);
			}

			@Override
			public TemplateExpression ne()
			{
				return ne(name);
			}

			@Override
			public TemplateExpression ne(final String variableName)
			{
				return addOperationField("$ne", variableName);
			}

			@Override
			public TemplateExpression gt()
			{
				return gt(name);
			}

			@Override
			public TemplateExpression gt(final String variableName)
			{
				return addOperationField("$gt", variableName);
			}

			@Override
			public TemplateExpression gte()
			{
				return gte(name);
			}

			@Override
			public TemplateExpression gte(final String variableName)
			{
				return addOperationField("$gte", variableName);
			}

			@Override
			public TemplateExpression lt()
			{
				return lt(name);
			}

			@Override
			public TemplateExpression lt(final String variableName)
			{
				return addOperationField("$lt", variableName);
			}

			@Override
			public TemplateExpression lte()
			{
				return lte(name);
			}

			@Override
			public TemplateExpression lte(final String variableName)
			{
				return addOperationField("$lte", variableName);
			}

			@Override
			public TemplateExpression in()
			{
				return in(name);
			}

			@Override
			public TemplateExpression in(final String variableName)
			{
				return addOperationField("$in", variableName);
			}

			@Override
			public TemplateExpression nin()
			{
				return nin(name);
			}

			@Override
			public TemplateExpression nin(final String variableName)
			{
				return addOperationField("$nin", variableName);
			}

			@Override
			public TemplateExpression exists()
			{
				return exists(name);
			}

			@Override
			public TemplateExpression exists(final String variableName)
			{
				final JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("$exists", true);

				final JsonObject parentObject = new JsonObject();
				parentObject.add(TemplateFieldOperation.convertNameToVariable(variableName), jsonObject);

				finder.conditionList.add(parentObject);
				variables.put(variableName,
				              new ExistsTemplateVariable(TemplateFieldOperation.convertNameToVariable(variableName), parentObject));

				return parentExpression;
			}

			private TemplateExpression addOperationField(final String operationName, final String variableName)
			{
				final JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty(operationName, TemplateFieldOperation.convertNameToVariable(variableName));
				return makeField(operationName, variableName, jsonObject);
			}

			private TemplateExpression makeField(final String fieldName, final String variableName, final JsonObject jsonObject)
			{
				final JsonObject parentObject = new JsonObject();
				parentObject.add(name, jsonObject);

				finder.conditionList.add(parentObject);
				variables.put(variableName, new TemplateVariable(fieldName, jsonObject));
				return parentExpression;
			}
		}

		public class VariableBinder
		{
			public VariableBinder bind(final String variableName, final String value)
			{
				variables.get(variableName).stream().forEach(e -> e.transform(variableName, value));
				return this;
			}

			public VariableBinder bind(final String variableName, final Number value)
			{
				checkArgument(!Strings.isNullOrEmpty(variableName));
				checkArgument(value != null);

				variables.get(variableName).stream().forEach(e -> e.transform(variableName, value));
				return this;
			}

			public VariableBinder bindStringCollection(final String variableName, final Collection<String> strings)
			{
				final JsonArray elementArray = new JsonArray();
				strings.stream().forEach(elementArray::add);

				variables.get(variableName).stream().forEach(e -> e.transform(variableName, elementArray));
				return this;
			}

			public VariableBinder bindNumberCollection(final String variableName, final Collection<Number> numbers)
			{
				final JsonArray jsonArray = new JsonArray();
				numbers.stream().forEach(jsonArray::add);

				variables.get(variableName).stream().forEach(e -> e.transform(variableName, jsonArray));
				return this;
			}

			public JsonObject toJson()
			{
				return finder.makeJson();
			}
		}
	}
}