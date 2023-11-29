import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * The LibraryManagementGUI class represents the graphical user interface for the Library Management System.
 * It provides functionalities to interact with a MySQL database, list books, remove books, and perform check-in
 * and check-out operations.
 */
public class LibraryManagementGUI extends JFrame {
    private final JTextArea outputTextArea;
    private final JTextField userInputField;
    private final Connection connection;

    /**
     * Constructs a LibraryManagementGUI object, initializes the GUI components, and establishes a connection to the database.
     * @throws SQLException if a database access error occurs
     */
    public LibraryManagementGUI() throws SQLException {
        // Connect to MySQL database
        String url = "jdbc:mysql://localhost:3306/LMS_Database";
        String user = "root";
        String password = "0603";
        connection = DriverManager.getConnection(url, user, password);

        // Set up the JFrame
        setTitle("Library Management System");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create a panel for user input
        JPanel userInputPanel = new JPanel();
        JLabel inputLabel = new JLabel("Enter barcode or title:");
        userInputField = new JTextField(20);

        // Create buttons for various actions
        JButton listBooksButton = new JButton("List All Books");
        JButton removeBarcodeButton = new JButton("Remove by Barcode");
        JButton removeTitleButton = new JButton("Remove by Title");
        JButton checkoutButton = new JButton("Check Out");
        JButton checkinButton = new JButton("Check In");
        JButton exitButton = new JButton("Exit");

        // Add components to the user input panel
        userInputPanel.add(inputLabel);
        userInputPanel.add(userInputField);
        userInputPanel.add(listBooksButton);
        userInputPanel.add(removeBarcodeButton);
        userInputPanel.add(removeTitleButton);
        userInputPanel.add(checkoutButton);
        userInputPanel.add(checkinButton);
        userInputPanel.add(exitButton);

        // Create a panel for output
        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // Create labels with the instructions
        JLabel infoLabel = new JLabel("Instructions:");
        JLabel infoLabel2 = new JLabel(" - Enter barcode or title for actions.");

        outputTextArea = new JTextArea(15, 30);
        outputTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        outputPanel.add(infoLabel);
        outputPanel.add(infoLabel2);
        outputPanel.add(scrollPane);

        // Add the panels to the main frame
        add(userInputPanel, BorderLayout.NORTH);
        add(outputPanel, BorderLayout.CENTER);

        // Add action listeners for buttons
        listBooksButton.addActionListener(new ListBooksListener());
        removeBarcodeButton.addActionListener(new RemoveBarcodeListener());
        removeTitleButton.addActionListener(new RemoveTitleListener());
        checkoutButton.addActionListener(new CheckoutListener());
        checkinButton.addActionListener(new CheckinListener());
        exitButton.addActionListener(new ExitListener());
    }

    /**
     * ActionListener for listing all books.
     */
    private class ListBooksListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            loadBooksFromDatabase();
        }
    }

    /**
     * ActionListener for removing a book by barcode.
     */
    private class RemoveBarcodeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String barcodeToRemove = userInputField.getText();
            int barcode = Integer.parseInt(barcodeToRemove);
            removeBookByBarcode(barcode);
        }
    }

    /**
     * ActionListener for removing a book by title.
     */
    private class RemoveTitleListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String titleToRemove = userInputField.getText();
            removeBookByTitle(titleToRemove);
        }
    }

    /**
     * ActionListener for checking out a book.
     */
    private class CheckoutListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String titleToCheckOut = userInputField.getText();
            checkOutBook(titleToCheckOut);
        }
    }

    /**
     * ActionListener for checking in a book.
     */
    private class CheckinListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String titleToCheckIn = userInputField.getText();
            checkInBook(titleToCheckIn);
        }
    }

    /**
     * ActionListener for exiting the application.
     */
    private class ExitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    /**
     * Loads books from the connected database and displays them in the GUI's output area.
     */
    private void loadBooksFromDatabase() {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM books")) {

            outputTextArea.setText("\n\nLoaded Books:\n");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String genre = resultSet.getString("genre");
                int barcode = resultSet.getInt("barcode");
                String status = resultSet.getString("status");
                String dueDate = resultSet.getString("due_date");

                // Update status based on due date
                if (dueDate != null && !dueDate.isEmpty()) {
                    status = "checked out";
                }

                outputTextArea.append("ID: " + id + ", Title: " + title + ", Author: " + author +
                        ", Genre: " + genre + ", Barcode: " + barcode + ", Status: " + status +
                        ", Due Date: " + dueDate + "\n");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error loading books from database: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Removes a book from the database by barcode.
     * @param barcode the barcode of the book to be removed
     */
    private void removeBookByBarcode(int barcode) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM books WHERE barcode = ?")) {
            preparedStatement.setInt(1, barcode);
            preparedStatement.executeUpdate();
            outputTextArea.setText("Book with barcode " + barcode + " removed.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error removing book: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Removes a book from the database by title.
     * @param title the title of the book to be removed
     */
    private void removeBookByTitle(String title) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM books WHERE title = ?")) {
            preparedStatement.setString(1, title);
            preparedStatement.executeUpdate();
            outputTextArea.setText("Book with title '" + title + "' removed.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error removing book: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Checks out a book by updating its status and due date in the database.
     * @param title the title of the book to be checked out
     */
    private void checkOutBook(String title) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE books SET status = 'checked out', due_date = ? WHERE title = ?")) {
            // Set due date to two weeks from today
            LocalDate currentDate = LocalDate.now();
            LocalDate dueDate = currentDate.plusWeeks(2);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDueDate = dueDate.format(formatter);

            preparedStatement.setString(1, formattedDueDate);
            preparedStatement.setString(2, title);
            preparedStatement.executeUpdate();

            outputTextArea.append("Book '" + title + "' checked out. Due date: " + formattedDueDate + "\n");

            // Schedule the loading of books after the current event
            SwingUtilities.invokeLater(this::loadBooksFromDatabase);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error checking out book: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Checks in a book by updating its status and removing the due date in the database.
     * @param title the title of the book to be checked in
     */
    private void checkInBook(String title) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE books SET status = 'checked in', due_date = NULL WHERE title = ?")) {
            preparedStatement.setString(1, title);
            preparedStatement.executeUpdate();
            outputTextArea.append("Book '" + title + "' checked in.\n");

            // Schedule the loading of books after the current event
            SwingUtilities.invokeLater(this::loadBooksFromDatabase);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error checking in book: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Entry point for the Library Management GUI application.
     *
     * @param args The command-line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                LibraryManagementGUI lms = new LibraryManagementGUI();
                lms.setVisible(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }
}