/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author maree
 */
import java.sql.*;
import java.util.Scanner;

public class LibraryManagement {

    static final String JDBC_URL = "jdbc:mysql://localhost:3306/library_db";
    static final String DB_USER = "root";
    static final String DB_PASS = "2910"; // Replace with your DB password

    public static void main(String[] args) {
        try (
            Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
            Scanner scanner = new Scanner(System.in)
        ) {
            while (true) {
                System.out.println("\n====== Library Management Menu ======");
                System.out.println("1. Get All Books");
                System.out.println("2. Find Books by Author");
                System.out.println("3. List All Borrowed Books");
                System.out.println("4. Count Books in Each Genre");
                System.out.println("5. Update Book Information");
                System.out.println("6. Add a New Member");
                System.out.println("7. Delete a Book");
                System.out.println("8. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1 -> getAllBooks(conn);
                    case 2 -> {
                        System.out.print("Enter Author Name: ");
                        String author = scanner.nextLine();
                        findBooksByAuthor(conn, author);
                    }
                    case 3 -> listBorrowedBooks(conn);
                    case 4 -> countBooksByGenre(conn);
                    case 5 -> {
                        System.out.print("Enter Book ID to update: ");
                        int id = scanner.nextInt(); scanner.nextLine();
                        System.out.print("New Title: ");
                        String title = scanner.nextLine();
                        System.out.print("New Author: ");
                        String author = scanner.nextLine();
                        System.out.print("New Genre: ");
                        String genre = scanner.nextLine();
                        updateBook(conn, id, title, author, genre);
                    }
                    case 6 -> {
                        System.out.print("Enter Member Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Email: ");
                        String email = scanner.nextLine();
                        System.out.print("Phone: ");
                        String phone = scanner.nextLine();
                        addNewMember(conn, name, email, phone);
                    }
                    case 7 -> {
                        System.out.print("Enter Book ID to delete: ");
                        int bookId = scanner.nextInt();
                        deleteBook(conn, bookId);
                    }
                    case 8 -> {
                        System.out.println("Exiting program.");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 1. Get All Books
    static void getAllBooks(Connection conn) throws SQLException {
        String query = "SELECT * FROM books";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("\nAll Books:");
            while (rs.next()) {
                System.out.printf("%d | %s | %s | %s%n",
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("genre"));
            }
        }
    }

    // 2. Find Books by Author
    static void findBooksByAuthor(Connection conn, String author) throws SQLException {
        String query = "SELECT * FROM books WHERE author = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, author);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("\nBooks by " + author + ":");
            while (rs.next()) {
                System.out.println(rs.getString("title"));
            }
        }
    }

    // 3. List All Borrowed Books
    static void listBorrowedBooks(Connection conn) throws SQLException {
        String query = """
            SELECT books.title, members.name, borrow.date_borrowed
            FROM borrow
            JOIN books ON borrow.book_id = books.id
            JOIN members ON borrow.member_id = members.id
            WHERE borrow.returned = false
        """;
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("\nBorrowed Books:");
            while (rs.next()) {
                System.out.printf("Book: %s | Borrowed by: %s | On: %s%n",
                    rs.getString("title"),
                    rs.getString("name"),
                    rs.getDate("date_borrowed"));
            }
        }
    }

    // 4. Count Books in Each Genre
    static void countBooksByGenre(Connection conn) throws SQLException {
        String query = "SELECT genre, COUNT(*) AS total_books FROM books GROUP BY genre";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("\nBooks by Genre:");
            while (rs.next()) {
                System.out.printf("%s: %d book(s)%n", rs.getString("genre"), rs.getInt("total_books"));
            }
        }
    }

    // 5. Update Book
    static void updateBook(Connection conn, int bookId, String title, String author, String genre) throws SQLException {
        String query = "UPDATE books SET title = ?, author = ?, genre = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setString(3, genre);
            pstmt.setInt(4, bookId);
            int rows = pstmt.executeUpdate();
            System.out.println(rows + " book(s) updated.");
        }
    }

    // 6. Add New Member
    static void addNewMember(Connection conn, String name, String email, String phone) throws SQLException {
        String query = "INSERT INTO members (name, email, phone) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, phone);
            int rows = pstmt.executeUpdate();
            System.out.println(rows + " member(s) added.");
        }
    }

    // 7. Delete Book
    static void deleteBook(Connection conn, int bookId) throws SQLException {
        String query = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bookId);
            int rows = pstmt.executeUpdate();
            System.out.println(rows + " book(s) deleted.");
        }
    }
}

