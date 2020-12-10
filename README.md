[![REUSE status](https://api.reuse.software/badge/github.com/SAP-samples/s4hana-ext-situation-handling)](https://api.reuse.software/info/github.com/SAP-samples/s4hana-ext-situation-handling)
# SAP S/4HANA Cloud Extensions: Situation Handling Automation
This repository contains the sample code for the [Situation Handling Automation tutorial](http://tiny.cc/s4-situation-handling).

*This code is only one part of the tutorial, so please follow the tutorial before attempting to use this code.*

## Description

The [Situation Handling Automation tutorial](http://tiny.cc/s4-situation-handling) uses the "Contract Is Ready as Source of Supply" situation type. The situation scenario covers contract handling in SAP S/4HANA Cloud.

Every time a contract is created or changed, the system automatically checks whether there are purchase requisitions without a source of supply. Persons responsible receive a notification and can assign the contract as a source of supply to the purchase requisitions.

With this extension sample, the manual assignment is automated based on business rules maintained in the SAP SCP rules engine using the context information of business documents. In the sample, the rule is based on the Material Group belonging to the requested product. 


#### SAP Extensibility Explorer

This tutorial is one of multiple tutorials that make up the [SAP Extensibility Explorer](https://sap.com/extends4) for SAP S/4HANA Cloud.
SAP Extensibility Explorer is a central place where anyone involved in the extensibility process can gain insight into various types of extensibility options. At the heart of SAP Extensibility Explorer, there is a rich repository of sample scenarios which show, in a hands-on way, how to realize an extensibility requirement leveraging different extensibility patterns.


Requirements
-------------
- An SAP Cloud Platform subaccount in the Neo environment with the Workflow, Business Rules and Portal service enabled and an SAP Cloud Platform Java server of any size.
- An SAP Cloud Platform subaccount in the Cloud Foundry environment with the Enterprise Messaging service enabled.
- An SAP S/4HANA Cloud tenant. **This is a commercial paid product.**
- [Java SE 8 Development Kit (JDK)](https://www.oracle.com/technetwork/java/javase/downloads/index.html) to compile the Java application.
- [Apache Maven](http://maven.apache.org/download.cgi) to build the Java application.

Download and Installation
-------------
This repository is a part of the [Download the Application](https://help.sap.com/viewer/160539efd20c4dcea8a1b945ae32500b/SHIP/en-US/26e92fe7b359467cbe10f243e7952a73.html) step in the tutorial. Instructions for use can be found in that step.

[Please download the zip file by clicking here](https://github.com/SAP/s4hana-ext-situation-handling/archive/master.zip) so that the code can be used in the tutorial.


Known issues
---------------------
If you are working with an SAP Cloud Platform _Trial_ account, you must add the following 2 properties to the destination so that the connection to SAP S/4HANA Cloud works:
```
proxyHost = proxy-trial.od.sap.biz
proxyPort = 8080
```

How to obtain support
---------------------
If you have issues with this sample, please open a report using [GitHub issues](https://github.com/SAP/s4hana-ext-situation-handling/issues).

License
-------
Copyright © 2020 SAP SE or an SAP affiliate company. All rights reserved.
This file is licensed under the Apache Software License, version 2.0 except as noted otherwise in the [LICENSE file](LICENSES/Apache-2.0.txt).
