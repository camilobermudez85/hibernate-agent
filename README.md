# hibernate-instrumentator
Jboss service that allows to expose the hibernate statistics mBean through the underlying JBoss JMX infrastructure.

The service will be exposed in JMX with the name,
```
com.hibernateinstrumentator:service=Instrumentator
```
and the operations
```
listSessionFactories()  // Lists all hibernate session factories available in JNDI.
instrument(String) // Exposes hibernate MBeans for a single session factory in JNDI.
instrumentAll()	// Exposes hibernate MBeans for every session factory found in JNDI.
```

after instrumenting a session factory, an MBean will be created with the name, 
```
hibernate.statistics:type={name}
```

where `name` is the JNDI name of the session factory with the `:` and `/` characters replaced by the dot(`.`) character.  

So far the project has been tested only on JBoss 4.2.3.

## Build
The project can be built using either maven or gradle. 

- Get your local copy of the project 
- Go to the root directory

### Gradle
- Run,
```
... hibernate-instrumentator]$ gradle sar
```
in the build/libs/ directory you'll find the *.sar file.

###Maven
- Run,
```
... hibernate-instrumentator]$ maven jar
```
in the target/ directory you'll find a *.jar file, change its extension to .sar and that is it.

- Deploy the .sar file to JBoss
