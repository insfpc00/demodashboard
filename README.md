# Demo Dashboard

Test web project for live sales monitoring  on a dashboard with a simple customer and product catalog management view.

The master of this repository is currently deployed on [Heroku](www.heroku.com) and available at https://fportodemodashboard.herokuapp.com

### Tech

This project integrates several technologies on top of Spring Boot, the most important are:

* [Thymeleaf] 
* [Spring websockets] 
* [Spring mail] 
* [Spring security]  
* [Bootstrap] 
* [jQuery] 
* [SockJS] 
* [Datatables]
* [AmCharts4]
* [Google Charts]
* [H2]

### Run the project

Requires Java 9+ and Maven 3.x to run.

Straight with Maven:

```sh
$ mvn clean spring-boot:run -Dspring-boot.run.arguments="initDb"
```
Build and run:

```sh
$ mvn clean package
$ java -jar target\demo-0.0.1-SNAPSHOT.war initDb
```
### Send data to the server
To simulate the sales that will be monitored on the dashboard you should make an HTTP POST request to https://fportodemodashboard.herokuapp.com/sales with data similar to this sample:
```sh
{
  "customerCIF": "V7792132H",
  "items":[{"productName": "Pendrive",
  "quantity": 2},{"productName": "Keyboard",
  "quantity": 2}]
  
}
```

### Automated tests
Simply run:
```sh
$ mvn clean test
```
