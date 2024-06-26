# Employee analyzer

> **Current version**
> 1.0

The aim of this application is to analyze organizational structure of employees and identify potential improvements.

Current version of application includes the following checks:

* every manager earns at least 20% more than the average salary of its direct subordinates, but no more than 50% more than that average
* employees have not more than 4 managers between them and the CEO

Information about organizational structure of employees should be provided as `csv file` (see detailed instructions below).

All found issues are printed out in terminal in the following format (example):

```
Found issues:
Employee ID: 124 [Martin Chekov], salary is below min limit (120.0%): -30.0% / -15000.00.
Employee ID: 125 [Bob Ronstad], salary is above max limit (150.0%): 7.0% / 2000.00.
Employee ID: 308 [Mihail Smirnov], salary is below min limit (120.0%): -20.0% / -4000.00, report line length is above max limit (4) by 1 additional level(s).
Employee ID: 309 [Dmitrii Dobrov], report line length is above max limit (4) by 2 additional level(s).
```

How to build the application
---

1. Clone git repository of the project `git clone https://github.com/alek-yam/employee-analyzer.git`
2. Run `mvn clean compile assembly:single` to build the application.
3. Once build is ready you can find executable JAR file (`employee-analyzer-<version>.jar`) in the `target` subfolder of the project.

How to use the application
---

> **Important**<br/>
> Java JRE version 17 or higher must be installed on your system.<br/>
> See more details on page [Get Java for desktop applications](https://www.java.com/en/)

The application is delivered as JAR file and can be run in terminal using the following command: 
```shell
java -jar employee-analyzer-<version>.jar -f <path to csv file>
```

Path to csv file is mandatory parameter to run the application.
It can be specified by adding one of the following arguments to the run command:
* `--filename <path to csv file>`
* `-f <path to csv file>`

Format of csv file should be the following (example):

```
Id,firstName,lastName,salary,managerId
123,Joe,Doe,60000,
124,Martin,Chekov,45000,123
125,Bob,Ronstad,47000,123
300,Alice,Hasacat,50000,124
305,Brett,Hardleaf,34000,300
```

Support
---
In case if you faced with any issues while using the application, you can ask for support by sending email to following address `aleksandr_yamskov@epam.com`