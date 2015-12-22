package com.pful.mongodb.querybuilder;

public class Field
{
	public static FieldOperation field(final String name)
	{
		return new FieldOperation(name);
	}

}