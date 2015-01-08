# hibernate-instrumentator
Expose the existing hibernate mBeans through the underlying JMX infrastructure (JVM or Application server).

So far the project supports only JBoss and has been tested only on JBoss 4.2.3.

## Build
The project can be built using either maven or gradle. 

Once you have your local copy of the project, go to the root directory and:

### Gradle
Run,
```
... hibernate-instrumentator]$ gradle sar

in the build/libs/ directory you'll find the *.sar file.

###Maven
Run,
```
... hibernate-instrumentator]$ maven jar

in the target/ directory you'll find a *.jar file, change its extension to .sar and that is it.

Deploy
