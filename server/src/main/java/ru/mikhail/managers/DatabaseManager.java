package ru.mikhail.managers;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import ru.mikhail.App;
import ru.mikhail.models.Chapter;
import ru.mikhail.models.Coordinates;
import ru.mikhail.models.SpaceMarine;
import ru.mikhail.models.Weapon;
import ru.mikhail.network.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class DatabaseManager {

    private Connection connection;
    private MessageDigest md;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrs" +
            "tuvwxyz0123456789<>?:@{!$%^&*()_+£$";
    private static final String PEPPER = "[g$J*(l;";
    private static final Logger databaseLogger = (Logger) LogManager.getLogger(DatabaseManager.class);

    public static final String HASHING_ALGORITHM = "SHA-512";
    public static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/studs";
    public static final String DATABASE_URL_HELIOS = "jdbc:postgresql://pg:5432/studs";
    public static final String DATABASE_CONFIG_PATH = Objects.requireNonNull(App.class.getClassLoader().getResource("config_to_server.cfg")).getPath();

    public DatabaseManager() {
        try {
            md = MessageDigest.getInstance(HASHING_ALGORITHM);

            this.connect();
            this.createMainBase();
        } catch (SQLException e) {
            databaseLogger.warn("Ошибка при исполнении изначального запроса либо таблицы уже созданы");
        } catch (NoSuchAlgorithmException e) {
            databaseLogger.fatal("Такого алгоритма нет!");
        }
    }

    public void connect() {
        Properties info = null;
        try {
            info = new Properties();
            info.load(new FileInputStream(DATABASE_CONFIG_PATH));
            connection = DriverManager.getConnection(DATABASE_URL, info);
            databaseLogger.info("Успешно подключен к базе данных");
        } catch (SQLException | IOException e) {
            try {

                connection = DriverManager.getConnection(DATABASE_URL_HELIOS, info);
            } catch (SQLException ex) {
                databaseLogger.fatal("Невозможно подключиться к базе данных");
                databaseLogger.debug(e);
                databaseLogger.debug(ex);
                System.exit(1);
            }
        }
    }

    public void createMainBase() throws SQLException {
        connection
                .prepareStatement(DatabaseCommands.allTablesCreation)
                .execute();
        databaseLogger.info("Таблицы созданы");
    }

    public void addUser(User user) throws SQLException {
        String login = user.name();
        String salt = this.generateRandomString();
        String pass = PEPPER + user.password() + salt;

        PreparedStatement ps = connection.prepareStatement(DatabaseCommands.addUser);
        if (this.checkExistUser(login)) throw new SQLException();
        ps.setString(1, login);
        ps.setString(2, this.getSHA512Hash(pass));
        ps.setString(3, salt);
        ps.execute();
        databaseLogger.info("Добавлен юзер " + user);
    }

    public boolean confirmUser(User inputUser) {
        try {
            String login = inputUser.name();
            PreparedStatement getUser = connection.prepareStatement(DatabaseCommands.getUser);
            getUser.setString(1, login);
            ResultSet resultSet = getUser.executeQuery();
            if (resultSet.next()) {
                String salt = resultSet.getString("salt");
                String toCheckPass = this.getSHA512Hash(PEPPER + inputUser.password() + salt);
                return toCheckPass.equals(resultSet.getString("password"));
            } else {
                return false;
            }
        } catch (SQLException e) {
            databaseLogger.fatal("Неверная команда sql!");
            databaseLogger.debug(e);
            return false;
        }
    }

    public boolean checkExistUser(String login) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(DatabaseCommands.getUser);
        ps.setString(1, login);
        ResultSet resultSet = ps.executeQuery();
        return resultSet.next();
    }

    // Метод возвращает -1 при ошибке добавления объекта
    public int addObject(SpaceMarine spaceMarine, User user) {
        try {
            LocalDateTime creationDateTime = spaceMarine.getCreationDate();
            Timestamp timestamp = Timestamp.valueOf(creationDateTime);

            PreparedStatement ps = connection.prepareStatement(DatabaseCommands.addObject);
            ps.setString(1, spaceMarine.getName());
            ps.setDouble(2, spaceMarine.getCoordinates().getX());
            ps.setDouble(3, spaceMarine.getCoordinates().getY());
            ps.setTimestamp(4, new java.sql.Timestamp(timestamp.getTime()));
            ps.setInt(5, spaceMarine.getHealth());
            ps.setString(6, spaceMarine.getAchievements());
            ps.setLong(7, spaceMarine.getHeight());
            ps.setObject(8, spaceMarine.getWeaponType(), Types.OTHER);
            ps.setString(9, spaceMarine.getChapter().getName());
            ps.setInt(10, spaceMarine.getChapter().getMarinesCount());
            ps.setString(11, user.name());
            ResultSet resultSet = ps.executeQuery();

            if (!resultSet.next()) {
                databaseLogger.info("Объект не добавлен в таблицу");
                return -1;
            }
            databaseLogger.info("Объект добавлен в таблицу");
            return resultSet.getInt(1);
        } catch (SQLException e) {
            databaseLogger.info("Объект не добавлен в таблицу");
            databaseLogger.debug(e);
            return -1;
        }
    }

    public boolean updateObject(int id, SpaceMarine spaceMarine, User user) {
        try {
            PreparedStatement ps = connection.prepareStatement(DatabaseCommands.updateUserObject);
            LocalDateTime creationDateTime = spaceMarine.getCreationDate();
            Timestamp timestamp = Timestamp.valueOf(creationDateTime);

            ps.setString(1, spaceMarine.getName());
            ps.setDouble(2, spaceMarine.getCoordinates().getX());
            ps.setDouble(3, spaceMarine.getCoordinates().getY());
            ps.setTimestamp(4, new java.sql.Timestamp(timestamp.getTime()));
            ps.setInt(5, spaceMarine.getHealth());
            ps.setString(6, spaceMarine.getAchievements());
            ps.setLong(7, spaceMarine.getHeight());
            ps.setObject(8, spaceMarine.getWeaponType(), Types.OTHER);
            ps.setString(9, spaceMarine.getChapter().getName());
            ps.setInt(10, spaceMarine.getChapter().getMarinesCount());

            ps.setInt(11, id);
            ps.setString(12, user.name());
            ResultSet resultSet = ps.executeQuery();
            System.out.println(resultSet);
            return resultSet.next();
        } catch (SQLException e) {
            databaseLogger.debug(e);
            return false;
        }
    }

    public boolean deleteObject(int id, User user) {
        try {
            PreparedStatement ps = connection.prepareStatement(DatabaseCommands.deleteUserObject);
            ps.setString(1, user.name());
            ps.setInt(2, id);
            ResultSet resultSet = ps.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            databaseLogger.error("Объект удалить не удалось");
            databaseLogger.debug(e);
            return false;
        }
    }

    public boolean deleteAllObjects(User user, List<Integer> ids) {
        try {
            for (Integer id : ids) {
                PreparedStatement ps = connection.prepareStatement(DatabaseCommands.deleteUserOwnedObjects);
                ps.setString(1, user.name());
                ps.setInt(2, id);
                ps.executeQuery();
            }
            databaseLogger.warn("Удалены все строки таблицы space_marine принадлежащие " + user.name());
            return true;
        } catch (SQLException e) {
            databaseLogger.error("Удалить строки таблицы space_marine не удалось!");
            databaseLogger.debug(e);
            return false;
        }
    }

    public PriorityQueue<SpaceMarine> loadCollection() {
        try {
            PreparedStatement ps = connection.prepareStatement(DatabaseCommands.getAllObjects);
            ResultSet resultSet = ps.executeQuery();
            PriorityQueue<SpaceMarine> collection = new PriorityQueue<>();
            while (resultSet.next()) {
                collection.add(new SpaceMarine(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        new Coordinates(
                                resultSet.getDouble("cord_x"),
                                resultSet.getDouble("cord_y")
                        ),
                        resultSet.getTimestamp("creation_date"),
                        resultSet.getInt("health"),
                        resultSet.getString("achievements"),
                        resultSet.getLong("height"),
                        Weapon.valueOf(resultSet.getString("weapon_type")),
                        new Chapter(
                                resultSet.getString("chapter_name"),
                                resultSet.getInt("chapter_marines_count")

                        ),
                        resultSet.getString("owner_login")
                ));
            }
            databaseLogger.info("Коллекция успешно загружена из таблицы");
            return collection;
        } catch (SQLException e) {
            databaseLogger.warn("Коллекция пуста либо возникла ошибка при исполнении запроса");
            return new PriorityQueue<>();
        }
    }

    private String generateRandomString() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    private String getSHA512Hash(String input) {
        byte[] inputBytes = input.getBytes();
        md.update(inputBytes);
        byte[] hashBytes = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
