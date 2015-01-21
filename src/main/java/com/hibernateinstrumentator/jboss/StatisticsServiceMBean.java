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

import java.util.List;
import java.util.Map;

/**
 * Interface that for every method that returns an array on the original
 * hibernate StatisticsServiceMBean, adds a new method that instead returns a
 * List with the same data, and for every method that returns a hibernate
 * specific object adds a new method that instead returns a Map with the same
 * data in the form Map<propertyName -> propertyValue>. The point with the maps
 * is not having to link to any hibernate library at the client side, and with
 * the strings lists, it's a workaround for an old issue in the after 1.5 JVMs
 * that makes it impossible to query array attributes in the way the JMX client
 * does, unless a JVM parameter is set on the client: in after 1.5 JVMs, the
 * parameter {@code Sun.lang.ClassLoader.allowArraySyntax} , which is loaded at
 * JVM initialization time, is {@code false} by default and causes the
 * ClassLoader to throw a {@code ClassNotFoundException} when asked to load an
 * array of primitive values or Strings in the way the JMX client does.
 * 
 * @author Camilo Bermudez <camilobermudez85@gmail.com>
 *
 */
public interface StatisticsServiceMBean extends
		org.hibernate.jmx.StatisticsServiceMBean {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.jmx.StatisticsServiceMBean#getEntityNames()
	 */
	public List<String> getEntityNamesList();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.jmx.StatisticsServiceMBean#getQueries()
	 */
	public List<String> getQueriesList();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.hibernate.jmx.StatisticsServiceMBean#getSecondLevelCacheRegionNames
	 * ()
	 */
	public List<String> getSecondLevelCacheRegionNamesList();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.jmx.StatisticsServiceMBean#getCollectionRoleNames()
	 */
	public List<String> getCollectionRoleNamesList();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.hibernate.stat.Statistics#getCollectionStatistics(java.lang.String)
	 */
	public Map<String, Object> getCollectionStatisticsMap(String role);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.stat.Statistics#getEntityStatistics(java.lang.String)
	 */
	public Map<String, Object> getEntityStatisticsMap(String entityName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.stat.Statistics#getQueryStatistics(java.lang.String)
	 */
	public Map<String, Object> getQueryStatisticsMap(String queryString);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.hibernate.stat.Statistics#getSecondLevelCacheStatistics(java.lang
	 * .String)
	 */
	public Map<String, Object> getSecondLevelCacheStatisticsMap(
			String regionName);

}
