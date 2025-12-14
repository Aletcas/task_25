package repository;

import model.User;
import utils.TestUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final Connection connection;

    public UserRepository(Connection connection) {
        this.connection = connection;
    }

    public void create(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.executeUpdate();
            TestUtils.logStep("Создан пользователь в БД: " + user.getUsername()); // <--
        }
    }

    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT username, password FROM users WHERE username = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User foundUser = new User(
                            resultSet.getString("username"),
                            resultSet.getString("password")
                    );
                    TestUtils.logStep("Найден пользователь в БД: " + username); // <--
                    return foundUser;
                }
            }
        }
        TestUtils.logStep("Пользователь не найден в БД: " + username); // <--
        return null;
    }

    public boolean deleteByUsername(String username) throws SQLException {
        String sql = "DELETE FROM users WHERE username = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            int rowsDeleted = statement.executeUpdate();
            boolean deleted = rowsDeleted > 0;

            if (deleted) {
                TestUtils.logStep("Удален пользователь из БД: " + username); // <--
            } else {
                TestUtils.logStep("Пользователь не найден для удаления: " + username); // <--
            }
            return deleted;
        }
    }

    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT username, password FROM users";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getString("username"),
                        resultSet.getString("password")
                ));
            }
        }
        TestUtils.logStep("Загружено пользователей из БД: " + users.size()); // <--
        return users;
    }

    public int deleteAllTestUsers() throws SQLException {
        String sql = "DELETE FROM users WHERE username LIKE 'test_user_%'";

        try (Statement statement = connection.createStatement()) {
            int rowsDeleted = statement.executeUpdate(sql);
            TestUtils.logStep("Удалено тестовых пользователей из БД: " + rowsDeleted); // <--
            return rowsDeleted;
        }
    }
}

