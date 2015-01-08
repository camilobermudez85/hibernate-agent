/*
 * Copyright 2015 Camilo Bermudez <camilobermudez85@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hibernateinstrumentator.jboss;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.Reference;

import org.hibernate.SessionFactory;
import org.hibernate.jmx.StatisticsService;
import org.jboss.system.ServiceMBeanSupport;

/**
 * Service implementation.
 * 
 * @author Camilo Bermudez <camilobermudez85@gmail.com>
 *
 */
public class Instrumentator extends ServiceMBeanSupport implements
		InstrumentatorMBean {

	private static final String STATISTICS_MBEAN_NAME_PATTERN = "hibernate.statistics:type={0}";
	// private static final String MANAGEMENT_MBEAN_NAME_PATTERN =
	// "hibernate.management:type={0}";

	private static final String SESSION_FACTORY_IMPL = "org.hibernate.impl.SessionFactoryImpl";

	private static final String[] SEARCHABLE_NAMESPACES = new String[] { "",
			"java:", "java:comp/" };

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hibernateinstrumentator.jboss.InstrumentatorMBean#listSessionFactories
	 * ()
	 */
	@Override
	public List<String> listSessionFactories() {

		try {
			return findSessionFactories();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hibernateinstrumentator.jboss.InstrumentatorMBean#instrumentAll()
	 */
	@Override
	public String instrumentAll() {

		try {

			List<String> sessionFactories = findSessionFactories();
			MBeanServer server = getServer();
			Context ctx = new InitialContext();
			for (String jndiName : sessionFactories) {
				instrument(server, ctx, jndiName);
			}
			log.info("All session factories have been instrumented.");
			return "All session factories have been instrumented.";

		} catch (Exception e) {
			e.printStackTrace();
			return "Error instrumenting session factory: " + e.getMessage();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hibernateinstrumentator.jboss.InstrumentatorMBean#instrument(java
	 * .lang.String)
	 */
	@Override
	public String instrument(String jndiName) {

		try {

			MBeanServer server = getServer();
			instrument(server, new InitialContext(), jndiName);
			return "Hibernate session factory " + jndiName
					+ " has been correctly instrumented.";

		} catch (Exception e) {
			e.printStackTrace();
			return "Error instrumenting session factory: " + e.getMessage();
		}

	}

	void instrument(MBeanServer server, Context ctx, String jndiName)
			throws NamingException, MalformedObjectNameException,
			InstanceAlreadyExistsException, MBeanRegistrationException,
			NotCompliantMBeanException {

		log.info("Instrumenting session factory " + jndiName);
		SessionFactory sessionFactory = (SessionFactory) ctx.lookup(jndiName);
		ObjectName statisticsMBeanName = new ObjectName(MessageFormat.format(
				STATISTICS_MBEAN_NAME_PATTERN, jndiName.replace(":", ".")
						.replace("/", ".")));

		StatisticsService service = new StatisticsService();
		service.setSessionFactory(sessionFactory);
		server.registerMBean(service, statisticsMBeanName);

		/*
		 * Can't publish the management mBean b/c haven't been able to get the
		 * implementation instance for org.hibernate.jmx.HibernateServiceMBean
		 * (assuming it is created by Jboss at some point), and b/c hibernate
		 * does not allow to create one by using an existing SessionFactory.
		 */

	}

	List<String> findSessionFactories() throws NamingException {

		InitialContext ctx = new InitialContext();
		List<String> sessionfactories = new ArrayList<String>();
		for (String ns : SEARCHABLE_NAMESPACES) {
			traverseNamespace((Context) ctx.lookup(ns), sessionfactories, ns);
		}
		return sessionfactories;

	}

	List<String> traverseNamespace(Context ctx, List<String> list, String baseNS)
			throws NamingException {

		NamingEnumeration<Binding> bindings = ctx.listBindings("");
		while (bindings.hasMore()) {
			Binding b = bindings.next();
			Object o = b.getObject();
			String className = o.getClass().getName();
			if (o instanceof Context) {
				return traverseNamespace((Context) o, list, baseNS);
			}
			if (o instanceof Reference) {
				Reference r = (Reference) o;
				className = r.getClassName();
			}
			if (SESSION_FACTORY_IMPL.equals(className)) {
				list.add(baseNS
						+ ctx.composeName(b.getName(), ctx.getNameInNamespace()));
			}
		}
		return list;

	}

}
