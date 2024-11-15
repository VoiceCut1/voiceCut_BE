package hackathon.voice_cut_1.voice_cut_1.annotation;

import hackathon.voice_cut_1.voice_cut_1.constraint_validator.VoiceFileConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = VoiceFileConstraintValidator.class)
public @interface VoiceFile {

    String message() default "must not be null or empty, and must have an .m4a extension";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
