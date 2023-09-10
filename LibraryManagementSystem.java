import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


/*
      Fernando Isidro Mendez
      CRN: Software Development I CEN-3042C
      Date: August 28,2023
      The function of the program represents an application for managing a library's collection of books.
      Users can list all existing books, add new books to the collection, and remove books.
      The book data is stored in a text file.
      The program aims to offer a simplified library management experience by providing
      interactive functionality to add, remove, and display books, while persisting book
      information in a text file for data retention.
      The class uses the 'Library' and 'Book' classes to manage book-related operations.
      The 'Library' class handles book storage and management, while the 'Book' class defines
      the structure of an individual book.

*/


public class LibraryManagementSystem {
    public static void main(String[] args) {
        Library library = new Library();

        // Load books from a text file
        loadBooksFromFile(library);

        Scanner scanner = new Scanner(System.in);

        /*

        This is what enables the simple menu of the application. This loop repeatedly display options
        to the user and will initiate the prompt with its corresponding case number

         */

        while (true) {
            System.out.println("\nLibrary Management System");
            System.out.println("1. List all books");
            System.out.println("2. Add a new book");
            System.out.println("3. Remove a book");
            System.out.println("4. Exit");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            /*
                **This is for future references**

                Note - I have not previously used switch statements  until I kept on encountering errors
                with my else if statements on this assignment. Switch was super easy to implement.

                https://www.baeldung.com/java-replace-if-statements

                **This is for future references**

             */

            switch (choice) {
                case 1 -> {
                    System.out.println("\nList of Books:");
                    library.listBooks();
                }
                case 2 -> {
                    System.out.print("\nPlease enter book details (ID, Title, Author): ");
                    String bookDetails = scanner.nextLine();
                    String[] parts = bookDetails.split(",");
                    if (parts.length == 3) {
                        int id = Integer.parseInt(parts[0]);
                        String title = parts[1];
                        String author = parts[2];
                        library.addBook(new Book(id, title, author));
                        writeBookToFile(new Book(id, title, author));
                        System.out.println("The book has been added successfully!");
                    } else {
                        System.out.println("Invalid input format.");
                    }
                }
                case 3 -> {
                    System.out.print("\nPlease enter the ID of the book to remove: ");
                    int idToRemove = scanner.nextInt();
                    library.removeBook(idToRemove);
                    System.out.println("The book has been removed successfully!");
                }
                case 4 -> {
                    System.out.println("Exiting Library Management System...");
                    scanner.close();
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please select a valid option from the list above...");
            }
        }
    }
/*

    This method reads book information from a text file and populates the 'library' object and 'book' object.

 */
    private static void loadBooksFromFile(Library library) {
        try (BufferedReader reader = new BufferedReader(new FileReader("E:\\IntelliJ IDEA 2023.2.1\\Books.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    int id = Integer.parseInt(parts[0]);
                    String title = parts[1];
                    String author = parts[2];
                    library.addBook(new Book(id, title, author));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading books from file: " + e.getMessage());
        }
    }

/*

    This method writes the information populated by the user into the text file.

 */
    private static void writeBookToFile(Book book) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("E:\\IntelliJ IDEA 2023.2.1\\Books.txt", true))) {
            String bookInfo = book.id() + "," + book.title() + "," + book.author();
            writer.write(bookInfo);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing book to file: " + e.getMessage());
        }
    }
}