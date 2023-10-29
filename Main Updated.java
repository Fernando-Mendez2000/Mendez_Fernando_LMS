import java.util.ArrayList;
import java.util.List;

record Book(int id, String title, String author, boolean isCheckedOut) {
    public Book(int id, String title, String author) {
        this(id, title, author, false);
    }

    @Override
    public String toString() {
        return id + "," + title + "," + author + "," + (isCheckedOut ? "checked out" : "available");
    }
}

class Library {
    private final List<Book> books;

    public Library() {
        books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBookByBarcode(int id) {
        books.removeIf(book -> book.id() == id);
    }

    public void removeBookByTitle(String title) {
        books.removeIf(book -> book.title().equalsIgnoreCase(title));
    }

    public void checkOutBook(String title) {
        Book book = findBookByTitle(title);
        if (book != null) {
            book = new Book(book.id(), book.title(), book.author(), true);
            updateBook(book);
        }
    }

    public Book findBookByTitle(String title) {
        for (Book book : books) {
            if (book.title().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null; // Book not found
    }

    public void checkInBook(String title) {
        Book book = findBookByTitle(title);
        if (book != null) {
            book = new Book(book.id(), book.title(), book.author(), false);
            updateBook(book);
        }
    }

    private void updateBook(Book updatedBook) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).id() == updatedBook.id()) {
                books.set(i, updatedBook);
                break;
            }
        }
    }

    public List<Book> getBooks() {
        return books;
    }
}