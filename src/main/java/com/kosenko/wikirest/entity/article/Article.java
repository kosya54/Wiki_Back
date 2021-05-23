package com.kosenko.wikirest.entity.article;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.kosenko.wikirest.entity.User;
import com.kosenko.wikirest.entity.Views;
import com.kosenko.wikirest.entity.article.elements.Heading;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "article")
public class Article {
    @Id
    @JsonView(Views.ArticleListView.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    @JsonView(Views.ArticleListView.class)
    private User user;

    @JsonView(Views.ArticleListView.class)
    private String title;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "article_id")
    @JsonView(Views.FullArticle.class)
    private List<Heading> headings;

    @ManyToMany
    @JoinTable(name = "articles_categories")
    @JsonView(Views.ArticleListView.class)
    private List<Category> categories;

    @Column(updatable = false)
    @JsonView(Views.ArticleListView.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime postedDate;

    public Article() {
        headings = new ArrayList<>();
        categories = new ArrayList<>();
    }

    public Article(User user, String title, List<Heading> headings, List<Category> categories) {
        this.user = user;
        this.title = title;
        this.headings = headings;

        this.categories = new ArrayList<>();
        this.categories.addAll(categories);

        postedDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Heading> getHeadings() {
        return headings;
    }

    public void setHeadings(List<Heading> heading) {
        this.headings = heading;
    }

    public LocalDateTime getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDateTime postedDate) {
        this.postedDate = postedDate;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategory(List<Category> categories) {
        this.categories = categories;
    }

/*    public void setCategory(Category category) {
        categories.add(category);
    } */

    /*    @Override
    public String toString() {
        return String.format("ID: %d, Title: %s, Content: %s, PostedDate: %s, UserName: %s",
                id, title, content, postedDate, user.getUsername());
    } */
}