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

import java.util.Arrays;
import java.util.List;

/**
 * Class that for every method that returns an array on the original hibernate
 * StatisticsService, adds a new method that instead returns a List with the
 * same data. This is a workaround for an old issue in the after 1.5 JVMs that
 * makes it impossible to query array attributes in the way the JMX client does,
 * unless a JVM parameter is set on the client: in after 1.5 JVMs, the parameter
 * {@code Sun.lang.ClassLoader.allowArraySyntax} , which is loaded at JVM
 * initialization time, is {@code false} by default and causes the ClassLoader
 * to throw a {@code ClassNotFoundException} when asked to load an array of
 * primitive values or Strings in the way the JMX client does.
 * 
 * @author Camilo Bermudez <camilobermudez85@gmail.com>
 *
 */
public class StatisticsService extends org.hibernate.jmx.StatisticsService
		implements StatisticsServiceMBean {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.jmx.StatisticsService#getEntityNames()
	 */
	public List<String> getEntityNamesList() {
		// TODO Auto-generated method stub
		return Arrays.asList(super.getEntityNames());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.jmx.StatisticsService#getQueries()
	 */
	public List<String> getQueriesList() {
		// TODO Auto-generated method stub
		return Arrays.asList(super.getQueries());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.jmx.StatisticsService#getSecondLevelCacheRegionNames()
	 */
	public List<String> getSecondLevelCacheRegionNamesList() {
		// TODO Auto-generated method stub
		return Arrays.asList(super.getSecondLevelCacheRegionNames());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.jmx.StatisticsService#getCollectionRoleNames()
	 */
	public List<String> getCollectionRoleNamesList() {
		// TODO Auto-generated method stub
		return Arrays.asList(super.getCollectionRoleNames());
	}

}
