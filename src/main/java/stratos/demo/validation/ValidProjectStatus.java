package stratos.demo.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ProjectStatusValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidProjectStatus {
    String message() default "Invalid project status";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}