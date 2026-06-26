package br.com.teste.dao;

import br.com.teste.exception.EmptyStorageException;
import br.com.teste.exception.UserNotFoundException;
import br.com.teste.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private long nextId =1L;

    private final List<UserModel> models = new ArrayList<>();

    public UserModel save(final UserModel model){
        model.setId(nextId++);
        models.add(model);
        return model;
    }

    public UserModel update(final UserModel model){
        var toUpdate = findById(model.getId());
        models.remove(toUpdate);
        models.add(model);
        return model;
    }

    public UserModel findById(final long id){
        var message = String.format("Não extiste usuário com o id cadastrado");
        return models.stream().filter(u -> u.getId() == id)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(message));
    }

    public void delete(final long id){
        var toDelete = findById(id);
        models.remove(toDelete);
    }

    public List<UserModel> findAll(){
        List<UserModel> result;
        try {
            verifyStorage();
            result = models;
        }catch (EmptyStorageException ex){
            ex.printStackTrace();
            result = new ArrayList<>();
        }
        return result;
    }

    private void verifyStorage(){
        if(models.isEmpty()) throw new EmptyStorageException("O Armazenamento está vazio");
    }


}
