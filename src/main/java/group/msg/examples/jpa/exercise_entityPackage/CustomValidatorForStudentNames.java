package group.msg.examples.jpa.exercise_entityPackage;

import group.msg.examples.jpa.validator.CustomValidation;

import javax.swing.plaf.synth.SynthTableUI;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class CustomValidatorForStudentNames implements ConstraintValidator<CustomNameValidation, String> {

    private boolean includeHardCodedValue;

    @Override
    public void initialize(CustomNameValidation validation) {
        includeHardCodedValue = validation.includeHardCodedValue();
        }

    @Override
    public boolean isValid(String studentFirstName, ConstraintValidatorContext validatorContext) {
        List<String> studentNames = new ArrayList<>();
        studentNames.add("someone");
        studentNames.add("someone1");
        return !(studentNames.contains(studentFirstName));
    }
}