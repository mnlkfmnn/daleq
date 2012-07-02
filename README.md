Introduction
------------

_Disclaimer: This README is work in progress, it is not yet finished._

Daleq is a DSL to define the content of a relational database in a concise and neat manner. It is actually very simple. 

It lets you describe your test data in your unit test where you just write 
those aspects of the data that are important for your test. The rest will be
filled out by Daleq:

```java
@Test
public void findBySize_should_returnThoseProductsHavingThatSize() {
    daleq.insertIntoDatabase(
            aTable(ProductTable.class)
                    .withRowsUntil(10)
                    .with(
                            aRow(10).f(SIZE, "S"),
                            aRow(11).f(SIZE, "S"),
                            aRow(12).f(SIZE, "M"),
                            aRow(13).f(SIZE, "L")
                    )
    );
    final List<Product> products = productDao.findBySize("S");

    assertProductsWithIds(products, 10L, 11L);
}
```
Obviously the test ensures that a query will filter products with a certain size. In this test we insert 14 rows into the table. The first ten rows have arbitrary content. It does not matter, which content they actually have, they just don't have a size S. Then we explicitly at 4 further rows, two of them having a size of S. In this test it does not matter either, which other columns are in the product table. This test is about the SIZE column and therefore we just focus on it. Daleq will do the rest.

Motivation
----------

Writing unit tests for SQL queries in a Java application stack is not easy. One of the challenges is setting up the test data on which the the query will run. 

To keep tests comprehensive and maintainable we have the following requirements for such data:

* **Data should be defined per test**. We think it is not maintainable to have a single dump, which is used for all tests. Each test ensures that a certain aspect of the query is implemented correctly. We doubt that it is possible to set up a single dump which contains all possible test cases. We doubt even more that such a dump stays maintainable in the long run.
* **Data should be defined close to the test**. The closer the data is to the test, the more likely is, that it stays maintainable. The closest the data can be to test is actually in the test. Hence test data has to be set up in same language the test is written in.
* **Data setup should only describe the aspects that matter** and therefore should be free of redundancy. Since relational database tables tend to contain a lot of repetitive data, setting up this data is poses a challenge if such tests should stay mantainable.

Concepts
--------

Let's have a look at Daleq's concept. They are actually very simple.

### Table Definitions
Assume you have such a database

```sql
CREATE TABLE PRODUCT (
  ID    INT  GENERATED BY DEFAULT AS SEQUENCE PRODUCT_SEQ,
  NAME  VARCHAR(250) NOT NULL,
  SIZE  VARCHAR(250) NOT NULL,
  PRICE DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (ID)
);
```
then you will have to model this table in Java as well. Simply write
```java
@TableDef("PRODUCT")
public class ProductTable {
    public static final FieldDef ID = Daleq.fd(DataType.INTEGER);
    public static final FieldDef NAME = Daleq.fd(DataType.VARCHAR);
    public static final FieldDef SIZE = Daleq.fd(DataType.VARCHAR);
    public static final FieldDef PRICE = Daleq.fd(DataType.DECIMAL);
}
```
and Daleq is ready to be used. 

### Tables
Now take a look at Daleq's DSL. It consists of two concepts: First there is a Table
```java
final Table table = Daleq.aTable(ProductTable.class);
```
Now we have an empty table. Ok, that does not help much. 

### Rows
Hence we a row:
```java
final Table table = Daleq.aTable(ProductTable.class).with(Daleq.aRow(1));
```


Examples
--------

For now have a look at the [tests in the example code](https://github.com/brands4friends/daleq/blob/master/examples/src/test/java/de/brands4friends/daleq/examples/JdbcProductDaoTest.java).

Authors
-------

**Lars Girndt**

Copyright and license
---------------------

Copyright 2012 brands4friends, Private Sale GmbH

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this work except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
