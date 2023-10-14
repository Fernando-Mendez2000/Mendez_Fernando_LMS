import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class LibraryManagementSystemTest {

    @Test
    public void testAddBookToDatabase() {
        Library library = new Library();
        Book book = new Book(1, "Sample Book", "Fernando Mendez");
        library.addBook(book);
        assertEquals(1, library.getBooks().size());
    }

    @Test
    public void testRemoveBookByBarcode() {
        Library library = new Library();
        Book book = new Book(1, "Sample Book", "Fernando Mendez");
        library.addBook(book);
        library.removeBookByBarcode(1);
        assertTrue(library.getBooks().isEmpty());
    }

    @Test
    public void testRemoveBookByTitle() {
        Library library = new Library();
        Book book = new Book(1, "Sample Book", "Fernando Mendez");
        library.addBook(book);
        library.removeBookByTitle("Sample Book");
        assertTrue(library.getBooks().isEmpty());
    }

    @Test
    public void testCheckOutBook() {
        Library library = new Library();
        Book book = new Book(1, "Sample Book", "Fernando Mendez");
        library.addBook(book);
        library.checkOutBook("Fernando Mendez");
        for (Book b : library.getBooks()) {
            if (b.title().equals("Fernando Mendez")) {
                assertTrue(b.isCheckedOut());
                break;
            }
        }
    }

    @Test
    public void testCheckInBook() {
        Library library = new Library();
        Book book = new Book(1, "Sample Book", "Fernando Mendez", true);
        library.addBook(book);
        library.checkInBook("Sample Book");
        for (Book b : library.getBooks()) {
            if (b.title().equals("Sample Book")) {
                assertFalse(b.isCheckedOut());
                break;
            }
        }
    }
}