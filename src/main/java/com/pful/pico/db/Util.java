package com.pful.pico.db;

import com.google.gson.JsonArray;

import java.util.Collection;

/**
 * Created by daeyeon on 12/31/15.
 */
public class Util
{
	public static <T extends String> JsonArray makeArrayToJsonArrayObject(final T[] array)
	{
		final JsonArray jsonArray = new JsonArray();
		for (final T v : array) {
			jsonArray.add(v);
		}
		return jsonArray;
	}

	public static <T extends Number> JsonArray makeArrayToJsonArrayObject(final T[] array)
	{
		final JsonArray jsonArray = new JsonArray();
		for (final T v : array) {
			jsonArray.add(v);
		}
		return jsonArray;
	}

	public static JsonArray makeNumberCollectionToJsonArrayObject(final Collection<Number> collection)
	{
		final JsonArray jsonArray = new JsonArray();
		collection.forEach(e -> jsonArray.add(e));
		return jsonArray;
	}

	public static JsonArray makeStringCollectionToJsonArrayObject(final Collection<String> collection)
	{
		final JsonArray jsonArray = new JsonArray();
		collection.forEach(e -> jsonArray.add(e));
		return jsonArray;
	}
}
