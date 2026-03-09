package vasyurin.work.services;

import vasyurin.work.services.interfaces.InputInConsoleValidator;

public class InputInConsoleValidatorImpl implements InputInConsoleValidator {

    @Override
    public boolean validate(String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}