# BPM Java Client - 12c
Write a Java BPM Client to get HumanWorkFlow and BPM context using HWF/BPM APIs in 12c

In 12c [12.1.3] the wlfullclient.jar and also some of classes, interfaces, methods are has been deprecated. 

The following jars are required to work with BPM APIs to develop worklist application.

Environment: 12.2.1 [Jdeveloper, SOA/BPM suite, Weblogic]

The required JDeveloper libraries are, 

 1. BPM Client
 2. WebLogic 12.1 Thin-Client
 3. JAX-WS Client
 4. Oracle XML Parser V2

These Jdev libraries are enough to create

 - Human WorkFlow service client
 - BPM service client
 - Workflow context
 - BPM context
 - TaskQueryService etc.

In this project, the main class, ***GetHWFBPMContext*** list out the above mentioned services. The ***ProcessUtils***, have some of useful methods to work with BPM worklist application.

