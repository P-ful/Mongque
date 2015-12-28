package com.pful.mongodb.querybuilder;

import java.util.Collection;

/**
 * Created by daeyeon on 12/28/15.
 */
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

	TReturn inStringCollection(final Collection<String> collection);

	TReturn inNumberCollection(final Collection<Number> collection);

	TReturn ninStringCollection(final Collection<String> collection);

	TReturn ninNumberCollection(final Collection<Number> collection);

	TReturn exists();
}
