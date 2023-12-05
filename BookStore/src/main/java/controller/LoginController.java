package controller;

import database.DatabaseConnectionFactory;
import database.Constants;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import model.User;
import model.validator.UserValidator;
import repository.book.BookRepository;
import repository.book.BookRepositoryCacheDecorator;
import repository.book.BookRepositoryMySQL;
import repository.book.Cache;
import service.book.BookService;
import service.book.BookServiceImpl;
import service.user.AuthenticationService;
import view.CustomerView;
import view.EmployeeView;
import view.LoginView;

import javax.xml.crypto.Data;
import java.util.EventListener;
import java.util.List;

public class LoginController {

    private final LoginView loginView;
    private final AuthenticationService authenticationService;
    private final UserValidator userValidator;


    public LoginController(LoginView loginView, AuthenticationService authenticationService, UserValidator userValidator) {
        this.loginView = loginView;
        this.authenticationService = authenticationService;
        this.userValidator = userValidator;

        this.loginView.addLoginButtonListener(new LoginButtonListener());
        this.loginView.addRegisterButtonListener(new RegisterButtonListener());
    }

    private class LoginButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            User user = authenticationService.login(username, password);

            if (user == null){
                loginView.setActionTargetText("Invalid Username or password!");
            }else{
                loginView.setActionTargetText("LogIn Successfull!");
                switch (user.getRoles().get(0).getRole()) {
                    case Constants.Roles.ADMINISTRATOR -> openAdminWindow();
                    case Constants.Roles.EMPLOYEE -> openEmployeeWindow();
                    default -> openCustomerWindow();
                }
            }

        }

        private void openCustomerWindow(){
            Stage customerStage = new Stage();
            CustomerView customerView = new CustomerView(customerStage);
            BookRepository bookRepository = new BookRepositoryCacheDecorator(
                    new BookRepositoryMySQL(DatabaseConnectionFactory.getConnectionWrapper(false).getConnection()),
                    new Cache<>()
            );
            BookService bookService = new BookServiceImpl(bookRepository);
            new CustomerController(customerView, bookService);
        }

        private void openEmployeeWindow(){
            Stage employeeStage = new Stage();
            EmployeeView employeeView = new EmployeeView(employeeStage);
            BookRepository bookRepository = new BookRepositoryCacheDecorator(
                    new BookRepositoryMySQL(DatabaseConnectionFactory.getConnectionWrapper(false).getConnection()),
                    new Cache<>()
            );
            BookService bookService = new BookServiceImpl(bookRepository);
            new EmployeeController(employeeView, bookService);
        }

        private void openAdminWindow(){

        }
    }

    private class RegisterButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            userValidator.validate(username, password);
            final List<String> errors = userValidator.getErrors();
            if (errors.isEmpty()) {
                if (authenticationService.register(username, password)){
                    loginView.setActionTargetText("Register successfull!");
                }else{
                    loginView.setActionTargetText("Register NOT successfull!");
                }
            } else {
                loginView.setActionTargetText(userValidator.getFormattedErrors());
            }
        }
    }
}