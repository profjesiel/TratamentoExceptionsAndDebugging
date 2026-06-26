import br.com.teste.dao.UserDAO;
import br.com.teste.exception.CustomException;
import br.com.teste.exception.EmptyStorageException;
import br.com.teste.exception.UserNotFoundException;
import br.com.teste.exception.ValidatorException;
import br.com.teste.model.MenuOption;
import br.com.teste.model.UserModel;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static br.com.teste.validator.UserValidator.verifyModel;


public class Main {

    private final static UserDAO dao = new UserDAO();
    private final static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        while (true) {
        System.out.println("\nBem vindo ao cadastro de usuário, selecione a operação desejada:" +
                "\n 1 - Cadastrar " +
                "\n 2 - Atualizar" +
                "\n 3 - Excluir" +
                "\n 4 - Buscar por identificador" +
                "\n 5 - Listar todos" +
                "\n 6 - Sair");
        var UserInput = sc.nextInt();


            var selectedOption = MenuOption.values()[UserInput - 1];
            switch (selectedOption){
                case SAVE -> {
                    try {
                        var user = dao.save(requestToSave());
                        System.out.printf("Usuario salvo %s", user);
                    }catch (CustomException ex){
                        System.out.println(ex.getMessage());
                        ex.printStackTrace();
                    } catch (ValidatorException e) {
                        throw new RuntimeException(e);
                    }
                }
                case UPDATE -> {
                    try {
                        var user = dao.update(requestToUpdate());
                        System.out.printf("Usuário atualizado %s", user);
                    }catch (UserNotFoundException | EmptyStorageException ex) {
                        System.out.println(ex.getMessage());
                    }catch (CustomException ex){
                        System.out.println(ex.getMessage());
                        ex.printStackTrace();
                    } catch (ValidatorException e) {
                        throw new RuntimeException(e);
                    } finally {
                        System.out.println("===========================");
                    }
                }
                case DELETE ->{
                    try {
                        dao.delete(requestId());
                        System.out.println("Usuário deletado");
                    }catch (UserNotFoundException | EmptyStorageException ex) {
                    System.out.println(ex.getMessage());
                    }
                }
                case FIND_BY_ID -> {
                    try {
                        var id = requestId();
                        var users = dao.findById(id);
                        System.out.printf("Usuário com id %s: ", id);
                        System.out.println(users);
                    } catch (UserNotFoundException | EmptyStorageException ex) {
                        System.out.println(ex.getMessage());
                    } finally {
                        System.out.println("=======================");
                    }
                }
                    case FIND_ALL ->{
                            var users = dao.findAll();
                            System.out.println("Usuários cadastrados" +
                                    "\n===============================:");
                            users.forEach(System.out::println);
                            System.out.println("==============FIM==============:");

                    }
                case EXIT -> System.exit(0);
            }
        }
    }
   private static UserModel requestToSave() throws ValidatorException {
       System.out.println("Informe o nome do usuário");
       var name = sc.next();
       System.out.println("Informe o email do usuário");
       var email = sc.next();
       System.out.println("Informe o data de nascimento do usuário (dd/MM/yyyy)");
       var birthdayString = sc.next();
       var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
       var birthday = LocalDate.parse(birthdayString, formatter);
       return validateInputs(0, name, email, birthday);

   }

   private static UserModel validateInputs(final long id, final String name,
                              final String email, final LocalDate birthday){
       var user = new UserModel(0, name, email, birthday);
       try {
           verifyModel(user);
           return user;
       }catch (ValidatorException ex){
           throw new CustomException("O seu usuario contém erros: " + ex.getMessage(), ex);
       }
   }

    private static UserModel requestToUpdate() throws ValidatorException {
        System.out.println("Informe o identificador do usuário");
        var id = sc.nextLong();
        System.out.println("Informe o nome do usuário");
        var name = sc.next();
        System.out.println("Informe o email do usuário");
        var email = sc.next();
        System.out.println("Informe o data de nascimento do usuário (dd/MM/yyyy)");
        var birthdayString = sc.next();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        var birthday = LocalDate.parse(birthdayString, formatter);
        return validateInputs(id, name, email, birthday);
    }

    private static long requestId(){
        System.out.println("Informe o identificador do usuário");
        return sc.nextLong();
    }
}