package com.kosenko.wikirest.service.impl;

import com.kosenko.wikirest.entity.article.Article;
import com.kosenko.wikirest.entity.article.Category;
import com.kosenko.wikirest.entity.article.elements.Heading;
import com.kosenko.wikirest.entity.dto.NewArticleDto;
import com.kosenko.wikirest.exception.EntityExistsException;
import com.kosenko.wikirest.exception.EntityNotFoundException;
import com.kosenko.wikirest.parsers.WordParser;
import com.kosenko.wikirest.repository.ArticleRepository;
import com.kosenko.wikirest.repository.CategoryRepository;
import com.kosenko.wikirest.service.ArticleService;
import com.kosenko.wikirest.service.StorageService;
import com.kosenko.wikirest.service.UserService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final StorageService storageService;
    private final UserService userService;
    private final CategoryRepository categoryRepository;
    private final WordParser wordParser;

    @Autowired
    public ArticleServiceImpl(
            ArticleRepository articleRepository,
            CategoryRepository categoryRepository,
            StorageService storageService,
            UserService userService,
            WordParser wordParser
    ) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
        this.storageService = storageService;
        this.userService = userService;
        this.wordParser = wordParser;
    }

    @Override
    public void addNewArticle(List<NewArticleDto> newArticleDtoList) {
        newArticleDtoList.forEach(newArticleDto -> {
            File file = storageService.getFileByFileName(newArticleDto.getFileName());
            String title = file.getName().substring(0, file.getName().lastIndexOf("."));

            if (articleRepository.existsArticleByTitle(title)) {
                throw new EntityExistsException("Статья с таким заголовком уже существует.");
            }

            List<Category> categories = new ArrayList<>();
            newArticleDto.getCategoryIds().forEach(categoryId -> categories.add(getCategoryById(categoryId)));

            try {
                List<Heading> headings = wordParser.parse(file);
                Article article = articleRepository.save(new Article(userService.getUserById(newArticleDto.getUserId()), title, headings, categories));
            } catch (IOException | InvalidFormatException ex) {
                ex.printStackTrace();
            }
        });
    }

    @Override
    public Article getArticleById(Long id) {
        checkArticleById(id);

        return articleRepository.findArticleById(id);
    }

    @Override
    public List<Article> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        if (articles.isEmpty()) {
            throw new EntityNotFoundException("Статьи не найдены.");
        }

        return articles;
    }

    @Override
    public List<Article> getArticlesByLimit(int page, int size) {
        List<Article> articles = articleRepository.findAllByOrderByPostedDateDesc(PageRequest.of(page, size));
        if (articles.isEmpty()) {
            throw new EntityNotFoundException("Статьи не найдены.");
        }

        return articles;
    }

    @Override
    public List<Article> getArticlesByCategoryId(Long id, int page, int size) {
        Category category = getCategoryById(id);
        List<Article> articles = articleRepository.findAllByCategoriesOrderByPostedDate(category, PageRequest.of(page, size));
        if (articles.isEmpty()) {
            throw new EntityNotFoundException("Статьи не найдены.");
        }

        return articles;
    }

    @Override
    public void deleteArticleById(Long id) {
        checkArticleById(id);

        Article article = articleRepository.findArticleById(id);
        List<String> imageNames = new ArrayList<>();
        article.getHeadings().forEach(heading -> heading.getParagraphs().forEach(paragraph -> imageNames.addAll(paragraph.getImageNames())));

        articleRepository.deleteById(id);

        if (!imageNames.isEmpty()) {
            imageNames.forEach(storageService::deleteImageByName);
        }

        storageService.deleteDocumentFileByArticleTitle(article.getTitle());
    }

    @Override
    public void deleteArticlesById(List<Long> ids) {
        ids.forEach(this::deleteArticleById);
    }

    private void checkArticleById(Long id) {
        if (!articleRepository.existsById(id)) {
            throw new EntityNotFoundException("Статья с id: " + id + " не найдена.");
        }
    }

//Категории
    @Override
    public Category addNewCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new EntityExistsException("Категория " + category.getName() + " уже существует.");
        }

        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(Long id) {
        checkCategoryById(id);

        return categoryRepository.findCategoryById(id);
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAllByOrderByNameAsc();
        if (categories.isEmpty()) {
            throw  new EntityNotFoundException("Категории не найдены");
        }

        return categories;
    }

    @Override
    public Category updateCategory(Long id, Category category) {
        checkCategoryById(id);

        Category categoryFromDb = getCategoryById(id);
        categoryFromDb.setName(category.getName());

        return categoryRepository.save(categoryFromDb);
    }

    @Override
    public void deleteCategoryById(Long id) {
        checkCategoryById(id);

        categoryRepository.deleteById(id);
    }

    private void checkCategoryById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Категория с id: " + id + " не найдена.");
        }
    }





/*    public List<String> upload(List<MultipartFile> multipartFiles) {
        return storageService.upload(multipartFiles);
    }


    public boolean isArticleExists(Long id) {
        return articleRepository.existsById(id);
    }

    public boolean isArticleExists(String title) {
//        return articleRepository.findArticlesByTitle(title) != null;
        return articleRepository.existsArticleByTitle(title);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAllByOrderByNameAsc();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findCategoryById(id);
    }

    public Category addNewCategory(String name) {
        return categoryRepository.save(new Category(name));
    }

    public Article getArticleById(Long id) {
        return articleRepository.findArticleById(id);
    }

    public List<Article> getArticlesByLimit(int page, int size) {
        return articleRepository.findAllByOrderByPostedDateDesc(PageRequest.of(page, size));
    }

    public List<Article> getArticlesByCategoryId(Long id) {
        Category category = getCategoryById(id);

        return articleRepository.findAllByCategories(category);
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public void deleteArticleById(Long id) {
        try {
            deleteArticleFilesByArticleId(id);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        articleRepository.deleteById(id);
    }

    private void deleteArticleFilesByArticleId(Long id) throws IOException {
        Article article = getArticleById(id);

        if (!storageService.deleteDocumentFileByName(article.getTitle())) {
            throw new IOException("Ошибка удаления файла");
        }

        List<Heading> headings = article.getHeadings();
        List<String> imageNames = new ArrayList<>();
        headings.forEach(heading -> {
            List<Paragraph> paragraphs = heading.getParagraphs();
            paragraphs.forEach(paragraph -> imageNames.addAll(paragraph.getImageNames()));
        });

        try {
            storageService.deleteImageFilesByName(imageNames);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //Отсюда продолжить
    public List<String> uploadFiles(List<MultipartFile> files) throws IOException {
        return getFileNameList(storageService.upload(files));
    }

    private Article addNewArticle(Article article) {
        return articleRepository.save(article);
    }

    public void createNewArticles(User user, String json) {
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(json);
        WordParser wordParser = new DocxParser();
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        jsonArray.forEach(element -> {
            JsonObject jsonObject = element.getAsJsonObject();

            JsonArray categoriesJsonArray = jsonObject.get("category").getAsJsonArray();
            List<Category> categories = new ArrayList<>();
            categoriesJsonArray.forEach(categoryJsonElement -> {
                Category category = categoryRepository.findCategoryById(categoryJsonElement.getAsLong());
                categories.add(category);
            });

            String fileName = jsonObject.get("name").getAsString();
            File file = null;
            try {
                file = getUploadedFile(fileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            if (file != null) {
                String title = file.getName().substring(0, file.getName().lastIndexOf("."));

                try {
                    List<Heading> headings = wordParser.parse(file);
                    Article article = addNewArticle(new Article(user, title, headings, categories));
                } catch (IOException | InvalidFormatException e) {
                    e.printStackTrace();
                }
            }
        });
    }



/*    public String getImagesDirPath() {
        return storageService.getUploadDirImg();
    } */

/*    private List<File> getUploadedFiles(List<String> uploadedFileNames) throws FileNotFoundException {
        return storageService.getFilesByName(uploadedFileNames);
    } */
/*
    private File getUploadedFile(String uploadedFileName) throws FileNotFoundException {
        return storageService.getFileByName(uploadedFileName);
    }

    private List<String> getFileNameList(List<MultipartFile> files) {
        List<String> fileNames = new ArrayList<>();
        files.forEach(file -> fileNames.add(file.getOriginalFilename()));

        return fileNames;
    }
*/
}
