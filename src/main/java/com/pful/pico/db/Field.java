package com.pful.pico.db;

public class Field
{
	public static FieldOperation field(final String name)
	{
		return new FieldOperation(name);
	}
}