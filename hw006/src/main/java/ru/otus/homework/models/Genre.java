package ru.otus.homework.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "genres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "genre", nullable = false, unique = true)
    private String genreName;

    @OneToMany(targetEntity = Book.class, orphanRemoval = true,
            cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "genre")
    private List<Book> books;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return id == genre.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
