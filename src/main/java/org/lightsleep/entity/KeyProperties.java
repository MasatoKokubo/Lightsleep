// KeyProperties.java
// (C) 2016 Masato Kokubo

package org.lightsleep.entity;

import java.lang.annotation.*;

/**
 * Indicates an array of <b>KeyProperty</b> annotations.
 *
 * @since 1.3.0
 * @author Masato Kokubo
 * @see Key
 * @see KeyProperty
 */
@Documented
// @Inherited // 1.5.1
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface KeyProperties {
	/** @return the array of <b>KeyProperty</b> annotations */
	KeyProperty[] value();
}
