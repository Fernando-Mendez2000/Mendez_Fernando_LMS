// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.

import java.util.ArrayList;
import java.util.List;

record Book(int id, String title, String author) {

    @Override
    public String toString() {
        return id + "," + title + "," + author;
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

    public void removeBook(int id) {
        books.removeIf(book -> book.id() == id);
    }

    public void listBooks() {
        for (Book book : books) {
            System.out.println(book);
        }
    }
}

