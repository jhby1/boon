/*
 * Copyright 2013-2014 Richard M. Hightower
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * __________                              _____          __   .__
 * \______   \ ____   ____   ____   /\    /     \ _____  |  | _|__| ____    ____
 *  |    |  _//  _ \ /  _ \ /    \  \/   /  \ /  \\__  \ |  |/ /  |/    \  / ___\
 *  |    |   (  <_> |  <_> )   |  \ /\  /    Y    \/ __ \|    <|  |   |  \/ /_/  >
 *  |______  /\____/ \____/|___|  / \/  \____|__  (____  /__|_ \__|___|  /\___  /
 *         \/                   \/              \/     \/     \/       \//_____/
 *      ____.                     ___________   _____    ______________.___.
 *     |    |____ ___  _______    \_   _____/  /  _  \  /   _____/\__  |   |
 *     |    \__  \\  \/ /\__  \    |    __)_  /  /_\  \ \_____  \  /   |   |
 * /\__|    |/ __ \\   /  / __ \_  |        \/    |    \/        \ \____   |
 * \________(____  /\_/  (____  / /_______  /\____|__  /_______  / / ______|
 *               \/           \/          \/         \/        \/  \/
 */

package org.boon.criteria.internal;


import org.boon.core.reflection.BeanUtils;
import org.boon.core.reflection.fields.FieldAccess;
import org.boon.core.Predicate;

import java.util.Collections;
import java.util.Map;

public abstract class Criteria implements Predicate {
    private static ThreadLocal<Map<String, FieldAccess>> fieldsLocal = new ThreadLocal<>();

    public static void fields( Map<String, FieldAccess> fields ) {
        fieldsLocal.set( fields );
    }

    public static void clearFields() {
        fieldsLocal.set( null );
    }

    public abstract void prepareForGroupTest( Map<String, FieldAccess> fields, Object owner );


    public abstract void cleanAfterGroupTest();

    public abstract boolean resolve( Map<String, FieldAccess> fields, Object owner );


    @Override
    public boolean test( Object o ) {
        Map<String, FieldAccess> fields = getFieldsInternal( o );
        return resolve( fields, o );
    }

    protected Map<String, FieldAccess> getFieldsInternal( Object o ) {
        if (o == null) {
            return Collections.EMPTY_MAP;
        }
        else {
            return getFieldsInternal( o.getClass(), o );
        }
    }


    protected Map<String, FieldAccess> getFieldsInternal( Class o ) {
        return getFieldsInternal( o.getClass(), null );
    }

    protected Map<String, FieldAccess> getFieldsInternal( Class clazz, Object o ) {
        Map<String, FieldAccess> fields = fieldsLocal == null ? null : fieldsLocal.get();
        if ( fields == null ) {
            if ( o != null ) {
                fields =  BeanUtils.getFieldsFromObject( o );
            } else {
                fields = BeanUtils.getFieldsFromObject( clazz );
            }
        }
        return fields;
    }

}
