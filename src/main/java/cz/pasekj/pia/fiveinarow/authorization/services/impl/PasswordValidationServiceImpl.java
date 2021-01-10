package cz.pasekj.pia.fiveinarow.authorization.services.impl;

import cz.pasekj.pia.fiveinarow.authorization.services.PasswordValidationService;
import org.passay.*;
import org.springframework.stereotype.Service;

/**
 * Service used for password strength validation validation
 * -    8-16 letters
 * -    one upper case
 * -    one lower case
 * -    one digit
 * -    one special
 * -    basic illegal sequences are forbidden
 * -    no whitespaces
 */
@Service("passwordValidationService")
public class PasswordValidationServiceImpl implements PasswordValidationService {

    /** PasswordValidator instance */
    private PasswordValidator validator;

    /**
     * Constructor - creates and configures the PasswordValidator
     */
    public PasswordValidationServiceImpl() {
        validator = new PasswordValidator(
                new LengthRule(8, 16),
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special, 1),
                new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 5, false),
                new IllegalSequenceRule(EnglishSequenceData.Numerical, 5, false),
                new IllegalSequenceRule(EnglishSequenceData.USQwerty, 5, false),
                new WhitespaceRule()
        );
    }

    @Override
    public boolean isValid(String password) {
        RuleResult result = validator.validate(new PasswordData(password));
        return result.isValid();
    }

}