import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Represents a simple library management system for managing a collection of books.
 * Users can list all existing books, add new books to the collection, remove books,
 * check out and check in books. The book data is stored in a text file.
 * The program aims to offer a simplified library management experience by providing
 * interactive functionality to perform various operations while persisting book
 * information in a text file for data retention.
 * The class uses the 'Library' and 'Book' classes to manage book-related operations.
 * The 'Library' class handles book storage and management, while the 'Book' class defines
 * the structure of an individual book.
 *
 * @author Fernando Isidro Mendez
 * @version Updated October 5, 2023
 * @see Library
 * @see Book
 */
public class LibraryManagementSystem {

    /**
     * The main method that initiates the library management system.
     *
     * @param args The command line arguments (not used in this program).
     */
    public static void main(String[] args) {
        Library library = new Library();
        Scanner scanner = new Scanner(System.in);
        loadBooksFromFile(library);

        /*
         * This loop repeatedly displays options to the user and initiates the
         * corresponding action based on the user's choice.
         */
        while (true) {
            System.out.println("\nLibrary Management System");
            System.out.println("1. List all books");
            System.out.println("2. Add a new book");
            System.out.println("3. Remove Book by Barcode");
            System.out.println("4. Remove Book by Title");
            System.out.println("5. Check Out Book");
            System.out.println("6. Check In Book");
            System.out.println("7. Exit");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.println("\nList of Books:");
                    library.getBooks().forEach(System.out::println);
                }
                case 2 -> {
                    System.out.print("\nEnter book details (ID, Title, Author): ");
                    String bookDetails = scanner.nextLine();
                    String[] parts = bookDetails.split(",");
                    if (parts.length == 3) {
                        int id = Integer.parseInt(parts[0].trim());
                        String title = parts[1].trim();
                        String author = parts[2].trim();
                        library.addBook(new Book(id, title, author));
                        writeBookToFile(new Book(id, title, author, false));
                        System.out.println("The book has been added successfully!");
                    } else {
                        System.out.println("Invalid input format.");
                    }
                }
                case 3 -> {
                    System.out.print("\nEnter Barcode of the Book to Remove: ");
                    int barcodeToRemove = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character
                    library.removeBookByBarcode(barcodeToRemove);
                    System.out.println("Book with barcode " + barcodeToRemove + " removed.");
                }
                case 4 -> {
                    System.out.print("\nEnter Title of the Book to Remove: ");
                    String titleToRemove = scanner.nextLine();
                    library.removeBookByTitle(titleToRemove);
                    System.out.println("Book with title '" + titleToRemove + "' removed.");
                }
                case 5 -> {
                    System.out.print("\nEnter Title of the Book to Check Out: ");
                    String titleToCheckOut = scanner.nextLine();
                    library.checkOutBook(titleToCheckOut);
                    System.out.println("Book '" + titleToCheckOut + "' checked out.");
                }
                case 6 -> {
                    System.out.print("\nEnter Title of the Book to Check In: ");
                    String titleToCheckIn = scanner.nextLine();
                    library.checkInBook(titleToCheckIn);
                    System.out.println("Book '" + titleToCheckIn + "' checked in.");
                }
                case 7 -> {
                    System.out.println("Exiting Library Management System...");
                    scanner.close();
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * This method reads book information from a text file and populates the 'library' object.
     *
     * @param library The library object to populate with book information.
     */
    private static void loadBooksFromFile(Library library) {
        try (BufferedReader reader = new BufferedReader(new FileReader("E:\\IntelliJ IDEA 2023.2.1\\Books.txt"))) {
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
        } catch (IOException e) {
            System.err.println("Error loading books from file: " + e.getMessage());
        }
    }

    /**
     * This method writes book information to a text file.
     *
     * @param book The book object whose information needs to be written to the file.
     */
    private static void writeBookToFile(Book book) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("E:\\IntelliJ IDEA 2023.2.1\\Books.txt", true))) {
            String bookInfo = book.id() + "," + book.title() + "," + book.author() + "," +
                    (book.isCheckedOut() ? "checked out" : "available");
            writer.write(bookInfo);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing book to file: " + e.getMessage());
        }
    }
}
