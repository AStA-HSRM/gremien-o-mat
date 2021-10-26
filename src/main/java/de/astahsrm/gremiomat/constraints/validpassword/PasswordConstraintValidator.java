package de.astahsrm.gremiomat.constraints.validpassword;

import java.util.Arrays;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.passay.DigitCharacterRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.SpecialCharacterRule;
import org.passay.UppercaseCharacterRule;
import org.passay.WhitespaceRule;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword password) {
        // Initializes Validator
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext cxt) {
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                // Between 8 and 30 chars, 1 uppercase, 1 number, 1 special char
                new LengthRule(8, 30), new UppercaseCharacterRule(1), new DigitCharacterRule(1),
                new SpecialCharacterRule(1), new WhitespaceRule()));

        RuleResult result = validator.validate(new PasswordData(password));
        return result.isValid();
    }
}
