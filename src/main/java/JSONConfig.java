import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.ToNumberPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface JSONConfig {
  boolean serializeNulls() default true;

  boolean prettyPrinting() default true;

  boolean excludeFieldsWithoutExposeAnnotation() default false;
/*
  FieldNamingPolicy namingPolicy() default FieldNamingPolicy.IDENTITY;

  ToNumberPolicy numberToNumberPolicy() default ToNumberPolicy.DOUBLE;

  ToNumberPolicy objectToNumberPolicy() default ToNumberPolicy.LAZILY_PARSED_NUMBER;

  LongSerializationPolicy longSerializationPolicy() default LongSerializationPolicy.DEFAULT;
*/
  boolean keepOldValuesWhenNotPresent() default false;
}