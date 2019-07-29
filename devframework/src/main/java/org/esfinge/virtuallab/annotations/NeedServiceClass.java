package org.esfinge.virtuallab.annotations;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.esfinge.virtuallab.annotations.logical.NeedServiceClassLogical;

import net.sf.esfinge.metadata.annotation.validator.ToValidate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
@ToValidate(NeedServiceClassLogical.class)
public @interface NeedServiceClass {

	 
}	