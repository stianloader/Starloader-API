package de.geolykt.starloader.impl;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks that a given method will be removed at compile time.
 * It should be used carefully, most often only when having to implement methods
 * that are already implemented by the superclass via mixins. The compiler may not know of certain things,
 * but you certainly do.
 *<br/>
 * <b>DO NOT USE THIS OUTSIDE OF THIS IMPLEMENTATION</b>
 *
 * @deprecated Usage of this annotation suggests bad design
 */
@Documented
@Retention(CLASS)
@Target(METHOD)
@Deprecated(forRemoval = true)
public @interface Pseudo { }
