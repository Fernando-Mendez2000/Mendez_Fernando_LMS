import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;

public class LibraryManagementGUI extends JFrame {
    private final Library library;
    private final JTextArea outputTextArea;
    private final JTextField userInputField;

    public LibraryManagementGUI() {
        // Initialize the Library
        library = new Library();

        // Set up the JFrame
        setTitle("Library Management System");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create a panel for user input
        JPanel userInputPanel = new JPanel();
        JLabel inputLabel = new JLabel("Enter file name, title, or book details:");
        userInputField = new JTextField(20);

        // Create buttons for various actions
        JButton loadFileButton = new JButton("Load File");
        JButton removeBarcodeButton = new JButton("Remove by Barcode");
        JButton removeTitleButton = new JButton("Remove by Title");
        JButton checkoutButton = new JButton("Check Out");
        JButton checkinButton = new JButton("Check In");
        JButton exitButton = new JButton("Exit");
        JButton addBookButton = new JButton("Add Book");

        // Add components to the user input panel
        userInputPanel.add(inputLabel);
        userInputPanel.add(userInputField);
        userInputPanel.add(loadFileButton);
        userInputPanel.add(removeBarcodeButton);
        userInputPanel.add(removeTitleButton);
        userInputPanel.add(checkoutButton);
        userInputPanel.add(checkinButton);
        userInputPanel.add(exitButton);
        userInputPanel.add(addBookButton);

        // Create a panel for output
        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Set the layout to left-align components

        // Create labels with the instructions
        JLabel infoLabel = new JLabel("Instructions:");
        JLabel infoLabel2 = new JLabel(" - To load a file, please place the file path in the box above. To check in" +
                " or out books, please enter the books title. To add a book, please use the following format" +
                " 'ID, Title, Author.' ");


        outputTextArea = new JTextArea(15, 30);
        outputTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        outputPanel.add(infoLabel); // Add the JLabels to the left
        outputPanel.add(infoLabel2);
        outputPanel.add(scrollPane);

        // Add the panels to the main frame
        add(userInputPanel, BorderLayout.NORTH);
        add(outputPanel, BorderLayout.CENTER);


        // Add action listeners for buttons
        loadFileButton.addActionListener(new LoadFileListener());
        removeBarcodeButton.addActionListener(new RemoveBarcodeListener());
        removeTitleButton.addActionListener(new RemoveTitleListener());
        checkoutButton.addActionListener(new CheckoutListener());
        checkinButton.addActionListener(new CheckinListener());
        exitButton.addActionListener(new ExitListener());
        addBookButton.addActionListener(new AddBookListener());
    }

    // Define action listeners for buttons
    private class LoadFileListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String fileName = userInputField.getText(); // Get the file name from the user input field
            try {
                loadBooksFromFile(library, fileName);
                outputTextArea.setText("Books loaded from file: " + fileName);

                // Append book data to the outputTextArea
                outputTextArea.append("\n\nLoaded Books:\n");
                for (Book book : library.getBooks()) {
                    outputTextArea.append(book.toString() + "\n");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error loading books from file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class RemoveBarcodeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String barcodeToRemove = userInputField.getText(); // Get the barcode from the user input field
            int barcode = Integer.parseInt(barcodeToRemove);
            library.removeBookByBarcode(barcode);
            outputTextArea.setText("Book with barcode " + barcode + " removed.");
        }
    }

    private class RemoveTitleListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String titleToRemove = userInputField.getText(); // Get the title from the user input field
            library.removeBookByTitle(titleToRemove);
            outputTextArea.setText("Book with title '" + titleToRemove + "' removed.");
        }
    }

    private class CheckoutListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String titleToCheckOut = userInputField.getText(); // Get the title from the user input field
            library.checkOutBook(titleToCheckOut);

            // Instead of setting the text, update the book data in the outputTextArea
            outputTextArea.append("\n\nUpdated Books:\n");
            for (Book book : library.getBooks()) {
                outputTextArea.append(book.toString() + "\n");
            }
        }
    }

    private class CheckinListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String titleToCheckIn = userInputField.getText(); // Get the title from the user input field
            library.checkInBook(titleToCheckIn);

            // Instead of setting the text, update the book data in the outputTextArea
            outputTextArea.append("\n\nUpdated Books:\n");
            for (Book book : library.getBooks()) {
                outputTextArea.append(book.toString() + "\n");
            }
        }
    }

    private class ExitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    private class AddBookListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String bookDetails = userInputField.getText().trim(); // Get book details from the user input field
            if (!bookDetails.isEmpty()) {
                String[] parts = bookDetails.split(",");
                if (parts.length == 3) {
                    try {
                        int id = Integer.parseInt(parts[0].trim());
                        String title = parts[1].trim();
                        String author = parts[2].trim();

                        // Check if book with the same ID already exists
                        boolean bookExists = false;
                        for (Book book : library.getBooks()) {
                            if (book.id() == id) {
                                bookExists = true;
                                break;
                            }
                        }

                        if (!bookExists) {
                            library.addBook(new Book(id, title, author, false));
                            writeBookToFile(new Book(id, title, author, false));
                            outputTextArea.setText("The book has been added successfully.");

                            // Update the displayed book list after adding
                            outputTextArea.append("\n\nUpdated Books:\n");
                            for (Book book : library.getBooks()) {
                                outputTextArea.append(book.toString() + "\n");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "A book with the same ID already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid book details. Please enter valid details.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid input format. Please enter book details in the format: ID, Title, Author.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please enter book details.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadBooksFromFile(Library library, String fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    int id = Integer.parseInt(parts[0].trim());
                    String title = parts[1].trim();
                    String author = parts[2].trim();
                    boolean isCheckedOut = parts[3].trim().equalsIgnoreCase("checked out");
                    library.addBook(new Book(id, title, author, isCheckedOut));
                }
            }
        }
    }

    private void writeBookToFile(Book book) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("E:\\IntelliJ IDEA 2023.2.1\\Books.txt", true))) {
            writer.write(book.id() + "," + book.title() + "," + book.author() + "," + (book.isCheckedOut() ? "checked out" : "available"));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibraryManagementGUI lms = new LibraryManagementGUI();
            lms.setVisible(true);
        });
    }
}