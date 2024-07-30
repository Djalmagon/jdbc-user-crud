
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.User;

public class UserDAO {

	private String jdbcURL = "jdbc:postgresql://localhost:5433/cadastro";
	private String jdbcUsername = "postgres";
	private String jdbcPassword = "admin";

	private static final String INSERT_USER_SQL = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
	private static final String SELECT_USER_BY_ID = "SELECT id, name, email, password FROM users WHERE id = ?";
	private static final String SELECT_ALL_USERS = "SELECT * FROM users";
	private static final String DELETE_USER_SQL = "DELETE FROM users WHERE id = ?";
	private static final String UPDATE_USER_SQL = "UPDATE users SET name = ?, email = ?, password = ? WHERE id = ?";

	protected Connection getConnection() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	public int insertUser(User user) throws SQLException {
		int generatedId = -1;

		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL,
						Statement.RETURN_GENERATED_KEYS)) {

			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getPassword());
			preparedStatement.executeUpdate();

			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					generatedId = generatedKeys.getInt(1);
				} else {
					throw new SQLException("Falha ao obter o ID do usuário.");
				}
			}
		}
		return generatedId;
	}

	public User selectUser(int id) {
		User user = null;

		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID)) {

			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				user = new User();
				user.setId(id);
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));
				user.setPassword(rs.getString("password"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public List<User> selectAllUsers() {
		List<User> users = new ArrayList<>();

		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS)) {

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));
				user.setPassword(rs.getString("password"));
				users.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}

	public boolean updateUser(User user) throws SQLException {
		boolean rowUpdated;

		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_SQL)) {

			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getPassword());
			preparedStatement.setInt(4, user.getId());

			rowUpdated = preparedStatement.executeUpdate() > 0;
		}
		return rowUpdated;
	}

	public boolean deleteUser(int id) throws SQLException {
		boolean rowDeleted;

		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_SQL)) {

			preparedStatement.setInt(1, id);
			rowDeleted = preparedStatement.executeUpdate() > 0;
		}
		return rowDeleted;
	}
}
