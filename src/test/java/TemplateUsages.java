import com.pful.pico.db.Field;
import com.pful.pico.db.Finder;
import com.pful.pico.db.QueryBuilderException;
import com.pful.pico.db.TemplateField;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by daeyeon on 12/28/15.
 */
public class TemplateUsages
{
	@Test
	public void templateUsage1()
			throws QueryBuilderException
	{
		Finder.registerTemplate("simple")
		      .allOf(Field.field("a").is("a"), TemplateField.field("b").is())
		      .templateField("field1").is();

		System.out.println(Finder.openQuery("simple")
		                         .bind("b", "value1").bind("field1", 10)
		                         .toJson());
	}

	@Test
	public void templateUsage2()
			throws QueryBuilderException
	{
		Finder.registerTemplate("simple")
		      .templateField("field1").is();

		System.out.println(Finder.openQuery("simple")
		                         .bind("field1", "value1")
		                         .toJson());
	}


	@Test
	public void simpleFieldUsageIsString()
			throws QueryBuilderException
	{
		System.out.println(Finder.registerTemplate("simple")
		                         .templateField("a").is()
		                         .toJson());

		System.out.println(Finder.openQuery("simple")
		                         .bind("a", "b")
		                         .toJson());
	}

	@Test
	public void simpleFieldUsageIsNumber()
			throws QueryBuilderException
	{
		System.out.println(Finder.registerTemplate("simple")
		                         .templateField("a").is()
		                         .toJson());

		System.out.println(Finder.openQuery("simple")
		                         .bind("a", 10)
		                         .toJson());
	}

	@Test
	public void simpleFieldUsageComplexIsStringOrNumber()
			throws QueryBuilderException
	{
		System.out.println(Finder.registerTemplate("simple1")
		                         .templateField("a").is()
		                         .templateField("c").is()
		                         .toJson());

		System.out.println(Finder.openQuery("simple1")
		                         .bind("a", "b")
		                         .bind("c", 10)
		                         .toJson());
	}

	@Test
	public void simpleFieldUsageGt()
			throws QueryBuilderException
	{
		System.out.println(Finder.registerTemplate("simple")
		                         .templateField("a").gt()
		                         .toJson());

		System.out.println(Finder.openQuery("simple")
		                         .bind("a", 10)
		                         .toJson());
	}

	@Test
	public void simpleFieldUsageLt()
			throws QueryBuilderException
	{
		System.out.println(Finder.registerTemplate("simple")
		                         .templateField("a").lt()
		                         .toJson());

		System.out.println(Finder.openQuery("simple")
		                         .bind("a", 10)
		                         .toJson());
	}

	@Test
	public void simpleFieldUsageGte()
			throws QueryBuilderException
	{
		System.out.println(Finder.registerTemplate("simple")
		                         .templateField("a").gte()
		                         .toJson());

		System.out.println(Finder.openQuery("simple")
		                         .bind("a", 10)
		                         .toJson());
	}

	@Test
	public void simpleFieldUsageLte()
			throws QueryBuilderException
	{
		System.out.println(Finder.registerTemplate("simple")
		                         .templateField("a").lte()
		                         .toJson());

		System.out.println(Finder.openQuery("simple")
		                         .bind("a", 10)
		                         .toJson());
	}

	@Test
	public void simpleFieldUsageIn()
			throws QueryBuilderException
	{
		System.out.println(Finder.registerTemplate("simple")
		                         .templateField("a").in()
		                         .toJson());

		System.out.println(Finder.openQuery("simple")
		                         .bindStringCollection(
				                         "a",
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
			throws QueryBuilderException
	{
		System.out.println(Finder.registerTemplate("simple")
		                         .templateField("a").nin()
		                         .toJson());

		System.out.println(Finder.openQuery("simple")
		                         .bindStringCollection(
				                         "a",
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
			throws QueryBuilderException
	{
		System.out.println(Finder.registerTemplate("simple")
		                         .templateField("a").exists()
		                         .toJson());

		System.out.println(Finder.openQuery("simple")
		                         .bind("a", "b")
		                         .toJson());
	}

	@Test
	public void combinedFieldUsages()
			throws QueryBuilderException
	{
		System.out.println(Finder.registerTemplate("simple")
		                         .templateField("a").is()
		                         .templateField("c").is()
		                         .templateField("e").gte()
		                         .templateField("f").in()
		                         .field("j").exists()
		                         .toJson());

		System.out.println(Finder.openQuery("simple")
		                         .bind("a", "b")
		                         .bind("c", "d")
		                         .bind("e", 10)
		                         .bindStringCollection("f",
		                                               new ArrayList<String>()
		                                               {{
			                                               add("g");
			                                               add("h");
			                                               add("i");
		                                               }})
		                         .toJson());
	}


	@Test
	public void simpleUsage2()
			throws QueryBuilderException
	{
		System.out.println(Finder.registerTemplate("simple")
		                         .templateField("a").is()
		                         .templateField("b").is()
		                         .toJson());

		System.out.println(Finder.openQuery("simple")
		                         .bind("a", "1")
		                         .bind("b", "2")
		                         .toJson());
	}

	@Test
	public void allOfUsages1()
			throws QueryBuilderException
	{
		System.out.println(Finder.registerTemplate("simple")
		                         .allOf(TemplateField.field("a").is())
		                         .toJson());

		System.out.println(Finder.openQuery("simple")
		                         .bind("a", "b")
		                         .toJson());
	}

	@Test
	public void allOfUsages2()
			throws QueryBuilderException
	{
		System.out.println(Finder.registerTemplate("simple")
		                         .allOf(TemplateField.field("a").is(),
		                                TemplateField.field("c").is())
		                         .toJson());

		System.out.println(Finder.openQuery("simple")
		                         .bind("a", "b")
		                         .bind("c", "d")
		                         .toJson());
	}

	@Test
	public void anyOfUsages1()
			throws QueryBuilderException
	{
		System.out.println(Finder.registerTemplate("simple")
		                         .anyOf(TemplateField.field("a").is(), TemplateField.field("c").is())
		                         .toJson());

		System.out.println(Finder.openQuery("simple")
		                         .bind("a", "b")
		                         .bind("c", "d")
		                         .toJson());
	}

	@Test
	public void noneOfUsages1()
			throws QueryBuilderException
	{
		System.out.println(Finder.registerTemplate("simple")
		                         .noneOf(TemplateField.field("a").is(), TemplateField.field("c").is())
		                         .toJson());

		System.out.println(Finder.openQuery("simple")
		                         .bind("a", "b")
		                         .bind("c", "d")
		                         .toJson());
	}

	@Test
	public void complexConditionalUsages1()
			throws QueryBuilderException
	{
		System.out.println(Finder.registerTemplate("simple")
		                         .anyOf(TemplateField.field("a").is(), TemplateField.field("b").gt())
		                         .noneOf(TemplateField.field("c").is(), TemplateField.field("d").is())
		                         .toJson());

		System.out.println(Finder.openQuery("simple")
		                         .bind("a", 10)
		                         .bind("b", 20)
		                         .bind("c", "v1")
		                         .bind("d", "v2")
		                         .toJson());
	}
}
