// NonUpdateProperties.java
// (C) 2016 Masato Kokubo

package org.lightsleep.entity;

import java.lang.annotation.*;

/**
 * Indicates an array of <b>NonUpdateProperty</b> annotations.
 *
 * @since 1.3.0
 * @author Masato Kokubo
 * @see NonUpdate
 * @see NonUpdateProperty
 */
@Documented
// @Inherited // 1.5.1
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface NonUpdateProperties {
	/** @return the array of <b>NonUpdateProperty</b> annotations */
	NonUpdateProperty[] value();
}
