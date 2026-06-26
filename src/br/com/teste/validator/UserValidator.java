package br.com.teste.validator;

import br.com.teste.exception.ValidatorException;
import br.com.teste.model.UserModel;

public class UserValidator {
    public UserValidator() {
    }

    public static void verifyModel(final UserModel model) throws ValidatorException{
        if(stringIsBlank(model.getName()))
              throw new ValidatorException("Informe um nome válido");
        if(model.getName().length() <= 1)
            throw new ValidatorException("O nome deve ter mais que 1 caracter");
        if(!model.getEmail().contains("@") || (!model.getEmail().contains(".")))
            throw new ValidatorException("Informe um e-mail válido");
    }

    private static boolean stringIsBlank(final String value){
        return value == null ||value.isBlank();
    }
}
