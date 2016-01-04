import com.pful.pico.db.Field;
import com.pful.pico.db.Finder;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by daeyeon on 12/22/15.
 */
public class UsageTest
{
	@Test
	public void simpleFieldUsageIsString()
	{
		System.out.println(
				Finder.newQuery()
				      .field("a").is("b")
				      .toJson());
	}

	@Test
	public void simpleFieldUsageIsNumber()
	{
		System.out.println(
				Finder.newQuery()
				      .field("a").is(10)
				      .toJson());
	}

	@Test
	public void simpleFieldUsageComplexIsStringOrNumber()
	{
		System.out.println(
				Finder.newQuery()
				      .field("a").is("b")
				      .field("c").is(10)
				      .toJson());
	}

	@Test
	public void simpleFieldUsageGt()
	{
		System.out.println(
				Finder.newQuery()
				      .field("a").gt(10)
				      .toJson());
	}

	@Test
	public void simpleFieldUsageLt()
	{
		System.out.println(
				Finder.newQuery()
				      .field("a").lt(10)
				      .toJson());
	}

	@Test
	public void simpleFieldUsageGte()
	{
		System.out.println(
				Finder.newQuery()
				      .field("a").gte(10)
				      .toJson());
	}

	@Test
	public void simpleFieldUsageLte()
	{
		System.out.println(
				Finder.newQuery()
				      .field("c").lte(10)
				      .toJson());
	}

	@Test
	public void simpleFieldUsageIn()
	{
		System.out.println(
				Finder.newQuery()
				      .field("a").inStringCollection(
						new ArrayList<String>()
						{{
							add("b");
							add("c");
							add("d");
						}})
				      .toJson());
	}

	@Test
	public void simpleFieldUsageNin()
	{
		System.out.println(
				Finder.newQuery()
				      .field("a").ninStringCollection(
						new ArrayList<String>()
						{{
							add("b");
							add("c");
							add("d");
						}})
				      .toJson());
	}

	@Test
	public void simpleFieldUsageExists()
	{
		System.out.println(
				Finder.newQuery()
				      .field("a").exists()
				      .toJson());
	}

	@Test
	public void combinedFieldUsages()
	{
		System.out.println(
				Finder.newQuery()
				      .field("a").is("b")
				      .field("c").is("d")
				      .field("e").gte(10)
				      .field("f").inStringCollection(
						new ArrayList<String>()
						{{
							add("g");
							add("h");
							add("i");
						}})
				      .field("j").exists()
				      .toJson());
	}


	@Test
	public void simpleUsage2()
	{
		System.out.println(
				Finder.newQuery()
				      .field("a").is("b")
				      .field("c").is("d")
				      .toJson());
	}

	@Test
	public void allOfUsages1()
	{
		System.out.println(
				Finder.newQuery()
				      .allOf(Field.field("a").is("b"))
				      .toJson());
	}

	@Test
	public void allOfUsages2()
	{
		System.out.println(
				Finder.newQuery()
				      .allOf(Field.field("a").is("b"), Field.field("c").is("d"))
				      .toJson());
	}

	@Test
	public void allOfUsages3()
	{
		System.out.println(
				Finder.newQuery()
				      .allOf(Field.field("a").is("b"), Field.field("c").is("d"))
				      .toJson());
	}

	@Test
	public void anyOfUsages1()
	{
		System.out.println(
				Finder.newQuery()
				      .anyOf(Field.field("a").is("b"), Field.field("c").is("d"))
				      .toJson());
	}

	@Test
	public void noneOfUsages1()
	{
		System.out.println(
				Finder.newQuery()
				      .noneOf(Field.field("a").is("b"), Field.field("c").is("d"))
				      .toJson());
	}

	@Test
	public void complexConditionalUsages1()
	{
		System.out.println(
				Finder.newQuery()
				      .anyOf(Field.field("a").is(10), Field.field("b").gt(10))
				      .noneOf(Field.field("a").is("b"), Field.field("c").is("d"))
				      .toJson());
	}
}
