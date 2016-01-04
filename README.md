# QueryBuilder-for-MongoDB-Gson
## Purpose

MongoDB uses JSON in order to query any document.
In some cases, it could give a difficulty to a developer because JSON object should be an nested object to express a complex query.
The nested JSON object could make source-code readability and maintainability less.
So some developers use officially built-in Query Builder[1] by MongoDB to improve them, but it is not easy to use.

**We aim two goals.**

1. Natural syntax to build a query for a Java developer.
2. Guaranteeing syntactic correctness at compile-time.

## Feature

- A syntax for building a query correctly.
- Query Template

### A syntax for building a query correctly

You can make any query following the syntax naturally.
We support operators in the list below.

- Comparison ($eq, $gt, $gte, $lt, $lte, $ne, $in, $nin)
- Logical ($or, $and, $nor)
- Element ($exists)
- Array ($all)

You can deliver your query built by QueryBuilder to MongoDB Driver.

### Query Template

In most cases, only variables are changed without any structural changes of a query.
Although the structure will be not modified, all the structure should be always rebuild for the variables.

So we provide 'Query Template'.
You can define a parametrized query and call it later.
Also you can bind your data to the variables.

## Dependency

- Google Gson 2.5
- Google Guava 19.0

## Limitation

We provide only Finder supporting find in MongoDB.
Also some operators such as evaluation or geo-spatial are not supported in this version.

## Next

We will provide Inserter, Updater and Deleter soon and unsupported operators.

## Tutorial

**Simple Query ($eq)**

```
Finder.newQuery()
      .field("age").is(32)
      .toJson()
```
```
Result 
{"a":10}
```

**Simple Query ($gte)**
```
Finder.newQuery()
      .field("age").gte(30)
      .toJson()
```
```
Result
{"age":{"$gte":30}}
```

**Simple Query ($and)**

```
Finder.newQuery()
      .allOf(Field.field("name").is("Peter"), 
             Field.field("major").is("Computer Science"))
      .toJson()
```
```
Result
{"$and":[{"name":"Peter"},{"major":"Computer Science"}]}
```

**Register a template**
```
Finder.registerTemplate("query_finding_men_with_age")
      .allOf(Field.field("sex").is("male"), 
             TemplateField.field("age").is())
```
or
```
Finder.registerTemplate("query_finding_men_with_age")
      .field("sex").is("male").
      .templateField("age").is();
```

**Call a template**
```
Finder.openQuery("query_finding_men_with_age")
      .bind("age", 30)
      .toJson()
```

## Reference
[1] Tutorials for an official Query Builder: http://mongodb.github.io/mongo-java-driver/2.13/getting-started/quick-tour/
