package ru.otus.homework.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "genres")
@Data
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

}
