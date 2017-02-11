// NonUpdateProperty.java
// (C) 2016 Masato Kokubo

package org.lightsleep.entity;

import java.lang.annotation.*;

/**
 * Indicates that the column related the field is not used in UPDATE SQL.<br>
 * Specifies the field by <b>value</b>.
 *
 * <div class="sampleTitle"><span>Example of use</span></div>
 * <div class="sampleCode"><pre>
 * {@literal @}InsertProperty(property="created", expression="CURRENT_TIMESTAMP")
 * <b>{@literal @}NonUpdateProperty("created")</b>
 * public class Contact {
 *
 *   public Timestamp created;
 * </pre></div>
 *
 * @since 1.3.0
 * @author Masato Kokubo
 * @see NonUpdate
 * @see NonUpdateProperties
 */
@Documented
// @Inherited // 1.5.1
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(NonUpdateProperties.class)
@Target({ElementType.TYPE})
public @interface NonUpdateProperty {
	/** @return the property name of the specified field */
	String value();
}
