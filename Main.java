import java.util.ArrayList;
import java.util.List;

/**
 * Represents a book in the library.
 *
 * @param id           The unique identifier of the book.
 * @param title        The title of the book.
 * @param author       The author of the book.
 * @param isCheckedOut Indicates whether the book is checked out or available.
 */
record Book(int id, String title, String author, boolean isCheckedOut) {

    /**
     * Constructs a new Book instance without specifying the check-out status.
     *
     * @param id     The unique identifier of the book.
     * @param title  The title of the book.
     * @param author The author of the book.
     */
    public Book(int id, String title, String author) {
        this(id, title, author, false);
    }

    /**
     * Returns a string representation of the book.
     *
     * @return A string containing the book's ID, title, author, and check-out status.
     */
    @Override
    public String toString() {
        return id + "," + title + "," + author + "," + (isCheckedOut ? "checked out" : "available");
    }
}

/**
 * Represents a library that manages a collection of books.
 */
class Library {
    private final List<Book> books;

    /**
     * Constructs a new Library instance.
     */
    public Library() {
        books = new ArrayList<>();
    }

    /**
     * Adds a book to the library.
     *
     * @param book The book to be added.
     */
    public void addBook(Book book) {
        books.add(book);
    }

    /**
     * Removes a book from the library based on its barcode (ID).
     *
     * @param id The barcode (ID) of the book to be removed.
     */
    public void removeBookByBarcode(int id) {
        books.removeIf(book -> book.id() == id);
    }

    /**
     * Removes a book from the library based on its title.
     *
     * @param title The title of the book to be removed.
     */
    public void removeBookByTitle(String title) {
        books.removeIf(book -> book.title().equalsIgnoreCase(title));
    }

    /**
     * Checks out a book by updating its status to "checked out."
     *
     * @param title The title of the book to be checked out.
     */
    public void checkOutBook(String title) {
        Book book = findBookByTitle(title);
        if (book != null) {
            book = new Book(book.id(), book.title(), book.author(), true);
            updateBook(book);
        }
    }

    /**
     * Finds a book in the library based on its title.
     *
     * @param title The title of the book to be found.
     * @return The Book object if found, or null if not found.
     */
    public Book findBookByTitle(String title) {
        for (Book book : books) {
            if (book.title().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null; // Book not found
    }

    /**
     * Checks in a book by updating its status to "checked in."
     *
     * @param title The title of the book to be checked in.
     */
    public void checkInBook(String title) {
        Book book = findBookByTitle(title);
        if (book != null) {
            book = new Book(book.id(), book.title(), book.author(), false);
            updateBook(book);
        }
    }

    /**
     * Updates the information of a book in the library.
     *
     * @param updatedBook The updated information of the book.
     */
    private void updateBook(Book updatedBook) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).id() == updatedBook.id()) {
                books.set(i, updatedBook);
                break;
            }
        }
    }

    /**
     * Gets the list of all books in the library.
     *
     * @return A list of Book objects representing all books in the library.
     */
    public List<Book> getBooks() {
        return books;
    }
}