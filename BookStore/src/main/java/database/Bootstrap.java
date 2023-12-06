package database;

import model.builder.BookBuilder;
import model.builder.UserBuilder;
import repository.book.BookRepository;
import repository.book.BookRepositoryCacheDecorator;
import repository.book.BookRepositoryMySQL;
import repository.book.Cache;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static database.Constants.Rights.RIGHTS;
import static database.Constants.Roles.ROLES;
import static database.Constants.Schemas.SCHEMAS;
import static database.Constants.getRolesRights;

import model.*;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceImpl;

// Script - code that automates some steps or processes

public class Bootstrap {

    private static RightsRolesRepository rightsRolesRepository;
    private static BookRepository bookRepository;
    private static UserRepository userRepository;

    public static void main(String[] args) throws SQLException {
        dropAll();

        bootstrapTables();

        bootstrapUserData();

        bootstrapBooks();
    }

    private static void dropAll() throws SQLException {
        for (String schema : SCHEMAS) {
            System.out.println("Dropping all tables in schema: " + schema);

            Connection connection = new JDBConnectionWrapper(schema).getConnection();
            Statement statement = connection.createStatement();

            String[] dropStatements = {
                    "TRUNCATE `role_right`;",
                    "DROP TABLE `role_right`;",
                    "TRUNCATE `right`;",
                    "DROP TABLE `right`;",
                    "TRUNCATE `user_role`;",
                    "DROP TABLE `user_role`;",
                    "TRUNCATE `role`;",
                    "DROP TABLE  `book`, `role`, `user`;"
            };

            Arrays.stream(dropStatements).forEach(dropStatement -> {
                try {
                    statement.execute(dropStatement);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }

        System.out.println("Done table bootstrap");
    }

    private static void bootstrapTables() throws SQLException {
        SQLTableCreationFactory sqlTableCreationFactory = new SQLTableCreationFactory();

        for (String schema : SCHEMAS) {
            System.out.println("Bootstrapping " + schema + " schema");


            JDBConnectionWrapper connectionWrapper = new JDBConnectionWrapper(schema);
            Connection connection = connectionWrapper.getConnection();

            Statement statement = connection.createStatement();

            for (String table : Constants.Tables.ORDERED_TABLES_FOR_CREATION) {
                String createTableSQL = sqlTableCreationFactory.getCreateSQLForTable(table);
                statement.execute(createTableSQL);
            }
        }

        System.out.println("Done table bootstrap");
    }

    private static void bootstrapUserData() throws SQLException {
        for (String schema : SCHEMAS) {
            System.out.println("Bootstrapping user data for " + schema);

            JDBConnectionWrapper connectionWrapper = new JDBConnectionWrapper(schema);
            rightsRolesRepository = new RightsRolesRepositoryMySQL(connectionWrapper.getConnection());
            userRepository = new UserRepositoryMySQL(connectionWrapper.getConnection(), rightsRolesRepository);

            bootstrapRoles();
            bootstrapRights();
            bootstrapRoleRight();
            bootstrapUserRoles();
        }
    }

    private static void bootstrapRoles() throws SQLException {
        for (String role : ROLES) {
            rightsRolesRepository.addRole(role);
        }
    }

    private static void bootstrapRights() throws SQLException {
        for (String right : RIGHTS) {
            rightsRolesRepository.addRight(right);
        }
    }

    private static void bootstrapRoleRight() throws SQLException {
        Map<String, List<String>> rolesRights = getRolesRights();

        for (String role : rolesRights.keySet()) {
            Long roleId = rightsRolesRepository.findRoleByTitle(role).getId();

            for (String right : rolesRights.get(role)) {
                Long rightId = rightsRolesRepository.findRightByTitle(right).getId();

                rightsRolesRepository.addRoleRight(roleId, rightId);
            }
        }
    }

    private static void bootstrapUserRoles() throws SQLException {
        List<Role> custRoles = Arrays.asList(rightsRolesRepository.findRoleByTitle(Constants.Roles.CUSTOMER));
        List<Role> emplRoles = Arrays.asList(rightsRolesRepository.findRoleByTitle(Constants.Roles.EMPLOYEE));
        List<Role> adminRoles = Arrays.asList(rightsRolesRepository.findRoleByTitle(Constants.Roles.ADMINISTRATOR));
        User customer1 = new UserBuilder()
                .setUsername("user1@gmail.com")
                .setPassword(hashPassword("abcdefgh123*"))
                .setRoles(custRoles)
                .build();

        User employee1 = new UserBuilder()
                .setUsername("employee1@gmail.com")
                .setPassword(hashPassword("randompass123*"))
                .setRoles(emplRoles)
                .build();

        User admin1 = new UserBuilder()
                .setUsername("bestAdmin@gmail.com")
                .setPassword(hashPassword("adminadmin123*"))
                .setRoles(adminRoles)
                .build();

        userRepository.save(customer1);
        userRepository.save(employee1);
        userRepository.save(admin1);
    }

    private static void bootstrapBooks() throws  SQLException{

        for (String schema : SCHEMAS){
            //Connection connection = new JDBConnectionWrapper(schema).getConnection();
            System.out.println("Bootstrapping books for " + schema + " schema");

            JDBConnectionWrapper connectionWrapper= new JDBConnectionWrapper(schema);
            bookRepository = new BookRepositoryCacheDecorator(
                    new BookRepositoryMySQL(connectionWrapper.getConnection()),
                    new Cache<>()
            );

            Book book1 = new BookBuilder(new PhysicalBook())
                    .setAuthor("Mihail Sadoveanu")
                    .setTitle("Baltagul")
                    .setPublishedDate(LocalDate.of(1930, Month.NOVEMBER, 1))
                    .setPrice(10)
                    .setStock(200)
                    .build();

            Book book2 = new BookBuilder(new PhysicalBook())
                    .setAuthor("Liviu Rebreanu")
                    .setTitle("Ion")
                    .setPublishedDate(LocalDate.of(1920, Month.NOVEMBER, 20))
                    .setPrice(15)
                    .setStock(150)
                    .build();

            Book book3 = new BookBuilder(new PhysicalBook())
                    .setAuthor("R.J. Palacio")
                    .setTitle("Minunea")
                    .setPublishedDate(LocalDate.of(2012, Month.APRIL, 1))
                    .setPrice(48)
                    .setStock(500)
                    .build();

            Book book4 = new BookBuilder(new PhysicalBook())
                    .setAuthor("Liz Pichon")
                    .setTitle("Tom Gates")
                    .setPublishedDate(LocalDate.of(2011, Month.APRIL, 1))
                    .setPrice(29)
                    .setStock(550)
                    .build();

            bookRepository.save(book1);
            bookRepository.save(book2);
            bookRepository.save(book3);
            bookRepository.save(book4);
        }
    }

    private static String hashPassword(String password) {
        try {
            // Sercured Hash Algorithm - 256
            // 1 byte = 8 biți
            // 1 byte = 1 char
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}