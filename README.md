# SQL Coding Challenge


## Steps

1. Check installation prerequisites: JDK version >=11, Maven latest version, your preferred IDE/editor. To see that everything basic is working, execute `mvn verify`, it should show you a `SUCCESS` build.
2. Read carefully through "Requirements" and "Out-of-scope" to focus on the right things.
3. IMPORTANT: After finishing coding, please add your comments and remarks inside this `README.md` file, see end of file. It helps us gaining context on how you reflect on the coding task.
4. Send back the result as mentioned inside the coding challenge mail

Coding guidance: Combine "clean, object-oriented, tested code" with "working software".


##  Requirements

* Structure and design [`Table.java`](/src/main/java/exercise/Table.java)
* Read and parse data from [`purchases.csv`](/src/main/resources/purchases.csv) and [`users.csv`](/src/main/resources/users.csv) and initialize to [`Table.java`](/src/main/java/exercise/Table.java)
* [`Table.java`](/src/main/java/exercise/Table.java) should be generic, column-names are inside .csv files and parsing should be based on this source.
* Implement an ORDER BY DESC in [`Sql.java#orderByDesc`](/src/main/java/exercise/Sql.java) ([SQL reference]( http://www.w3schools.com/sql/sql_orderby.asp))
* Implement an INNER JOIN in [`Sql.java#join`](/src/main/java/exercise/Sql.java) ([SQL reference]( https://www.w3schools.com/sql/sql_join.asp))
* You are not restricted to implement everything in the existing file/folder structure. Feel free to add or alter files as you see fit.


## Out-of-scope

* NO "real" persistence necessary (do not integrate MySQL, HSQLDB, h2database, or JPA). Use your own internal storage model (see also [`Table.java`](/src/main/java/exercise/Table.java)).
* NO need to implement any SQL parser or anything, Java code interface is sufficient.
* NO need to be Maven expert or adapt build, project harness and build is setup already.
* NO Javadoc necessary. Tests and self explaining code are sufficient.


## Your comments / remarks

What would you do if you had more time? Which trade-offs did you take?

* support escaping in csv file
* improve performance- reduce unnecessary iterations over rows, e.g. in Table-Constructor, maybe convert to lazy.
* improve performance- cache some lookups, such as Table::getHeader(name)
* improve performance- add indexed columns so e.g. join has better complexity then n².
* have a better api, maybe a fluent one on table-objects, so the code that works with tables is more readable. (e.g. table.innerJoin(otherTable...) instead of sql.join(table1, table2,...). 
* having these methods on the table-class would also make switching db easy without changing much. tables could be either in memory using java-streams for logic or can be in an actual database, transparent to the user.
* also more convenience functions (such as table::getColumn, table::getRow(idx)) but these were not necessary for the presented requirements




Have fun!
