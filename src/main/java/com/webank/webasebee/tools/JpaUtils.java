/**
 * Copyright 2014-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.webank.webasebee.tools;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;

/**
 * JpaUtils
 *
 * @Description: JpaUtils
 * @author maojiayu
 * @data Dec 21, 2018 10:50:38 AM
 *
 */
public class JpaUtils {
    public static Predicate andTogether(List<Predicate> ps, CriteriaBuilder cb) {
        return cb.and(ps.toArray(new Predicate[0]));
    }

    public static Predicate orTogether(List<Predicate> ps, CriteriaBuilder cb) {
        return cb.or(ps.toArray(new Predicate[0]));
    }
}