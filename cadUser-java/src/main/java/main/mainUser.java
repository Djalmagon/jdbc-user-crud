
package main;

import java.sql.SQLException;
import java.util.List;

import dao.UserDAO;
import model.User;

public class mainUser {
	public static void main(String[] args) {
		UserDAO userDAO = new UserDAO();

		User newUser1 = new User();
		newUser1.setName("John Doe");
		newUser1.setEmail("John.doe@gmail.com");
		newUser1.setPassword("pass1234");

		User newUser2 = new User();
		newUser2.setName("Jane Doe");
		newUser2.setEmail("Jane.doe@gmail.com");
		newUser2.setPassword("new1234");

		try {
			int id1 = userDAO.insertUser(newUser1);
			int id2 = userDAO.insertUser(newUser2);

			System.out.println("Usuários inseridos com IDs: " + id1 + " e " + id2);
		} catch (SQLException e) {
			System.err.println("Erro ao inserir o usuário: " + e.getMessage());
			e.printStackTrace();
		}

		List<User> users = userDAO.selectAllUsers();
		System.out.println("Lista de usuários:");
		for (User u : users) {
			System.out.println(
					u.getId() + ";\"" + u.getName() + "\";\"" + u.getEmail() + "\";\"" + u.getPassword() + "\"");
		}

		User user = userDAO.selectUser(1);
		if (user != null) {
			user.setName("Jane Doe Updated");
			user.setEmail("Jane.updated@gmail.com");
			user.setPassword("updated1234");
			try {
				boolean updated = userDAO.updateUser(user);

				if (updated) {
					System.out.println("Usuário atualizado com sucesso.");
				} else {
					System.out.println("Falha ao atualizar o usuário.");
				}
			} catch (SQLException e) {
				System.err.println("Erro ao atualizar o usuário: " + e.getMessage());
				e.printStackTrace();
			}

			users = userDAO.selectAllUsers();
			System.out.println("Lista de usuários após atualização:");
			for (User u : users) {
				System.out.println(
						u.getId() + ";\"" + u.getName() + "\";\"" + u.getEmail() + "\";\"" + u.getPassword() + "\"");
			}
		}
	}
}
