# QueryBuilder-for-MongoDB-Gson
## Purpose

MongoDB uses JSON in order to query any document.
In some cases, it could give a difficulty to a developer especially when JSON object should be a nested form to express a complex query.
Such nested JSON objects could make source-code readability and maintainability worse.
So some developers try to use the built-in, official Query Builder by MongoDB [1], but it is still not so easy.

**We aim two goals.**

1. Natural syntax to build a query for a Java developer
2. Guaranteeing syntactic correctness at compile-time

## Feature

- Building Valid Queries
- Query Template

### Building Valid Queries

You can make any syntatically valid query by following the syntaxes naturally.
We support operators in the list below.

- Comparison ($eq, $gt, $gte, $lt, $lte, $ne, $in, $nin)
- Logical ($or, $and, $nor)
- Element ($exists)
- Array ($all)

You can deliver your query built by QueryBuilder to MongoDB Driver.

### Query Template

In most queries, only variable values are changed with no structural change at all.
Developers have to write the same structure again and again for the variable values.

So we provide 'Query Template' to lesson this repetitive task.
You can define a parametrised query and call it later.
Also you can bind your data to the variables.

## Dependency

- Google Gson 2.5
- Google Guava 19.0

## Limitation

We provide only 'Finder' supporting the find method in MongoDB.
Also some operators such as evaluation or geo-spatial are not yet supported in this version.

## Next

We will provide Inserter, Updater, Deleter, and unsupported operators continuously.

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
