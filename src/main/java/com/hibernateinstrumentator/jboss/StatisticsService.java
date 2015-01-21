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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.stat.CollectionStatistics;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.QueryStatistics;
import org.hibernate.stat.SecondLevelCacheStatistics;

/**
 * Class that for every method that returns an array on the original hibernate
 * StatisticsService, adds a new method that instead returns a List with the
 * same data, and for every method that returns a hibernate specific object adds
 * a new method that instead returns a Map with the same data in the form
 * Map<propertyName -> propertyValue>. The point with the maps is not having to
 * link to any hibernate library at the client side, and with the strings lists,
 * it's a workaround for an old issue in the after 1.5 JVMs that makes it
 * impossible to query array attributes in the way the JMX client does, unless a
 * JVM parameter is set on the client: in after 1.5 JVMs, the parameter
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hibernateinstrumentator.jboss.StatisticsServiceMBean#
	 * getCollectionStatisticsMap(java.lang.String)
	 */
	@Override
	public Map<String, Object> getCollectionStatisticsMap(String role) {

		Map<String, Object> m = null;
		CollectionStatistics cs = super.getCollectionStatistics(role);
		if (cs != null) {
			m = new HashMap<String, Object>();
			m.put("fetchCount", cs.getFetchCount());
			m.put("loadCount", cs.getLoadCount());
			m.put("recreateCount", cs.getRecreateCount());
			m.put("removeCount", cs.getRemoveCount());
			m.put("updateCount", cs.getUpdateCount());
		}

		return m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hibernateinstrumentator.jboss.StatisticsServiceMBean#
	 * getEntityStatisticsMap(java.lang.String)
	 */
	@Override
	public Map<String, Object> getEntityStatisticsMap(String entityName) {

		Map<String, Object> m = null;
		EntityStatistics es = super.getEntityStatistics(entityName);
		if (es != null) {
			m = new HashMap<String, Object>();
			m.put("deleteCount", es.getDeleteCount());
			m.put("fetchCount", es.getFetchCount());
			m.put("insertCount", es.getInsertCount());
			m.put("loadCount", es.getLoadCount());
			m.put("updateCount", es.getUpdateCount());
		}

		return m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hibernateinstrumentator.jboss.StatisticsServiceMBean#
	 * getQueryStatisticsMap(java.lang.String)
	 */
	@Override
	public Map<String, Object> getQueryStatisticsMap(String queryString) {

		Map<String, Object> m = null;
		QueryStatistics qs = super.getQueryStatistics(queryString);
		if (qs != null) {
			m = new HashMap<String, Object>();
			m.put("cacheHitCount", qs.getCacheHitCount());
			m.put("cacheMissCount", qs.getCacheMissCount());
			m.put("cachePutCount", qs.getCachePutCount());
			m.put("executionAvgTime", qs.getExecutionAvgTime());
			m.put("executionTime", qs.getExecutionCount());
			m.put("executionMaxTime", qs.getExecutionMaxTime());
			m.put("executionMinTime", qs.getExecutionMinTime());
			m.put("executionRowCount", qs.getExecutionRowCount());
		}

		return m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hibernateinstrumentator.jboss.StatisticsServiceMBean#
	 * getSecondLevelCacheStatisticsMap(java.lang.String)
	 */
	@Override
	public Map<String, Object> getSecondLevelCacheStatisticsMap(
			String regionName) {

		Map<String, Object> m = null;
		SecondLevelCacheStatistics slcs = super
				.getSecondLevelCacheStatistics(regionName);
		if (slcs != null) {
			m = new HashMap<String, Object>();
			m.put("elementCountInMemory", slcs.getElementCountInMemory());
			m.put("elementCountOnDisk", slcs.getElementCountOnDisk());
			m.put("entries", slcs.getEntries());
			m.put("hitCount", slcs.getHitCount());
			m.put("missCount", slcs.getMissCount());
			m.put("putCount", slcs.getPutCount());
			m.put("sizeInMemory", slcs.getSizeInMemory());
		}

		return m;
	}

}
