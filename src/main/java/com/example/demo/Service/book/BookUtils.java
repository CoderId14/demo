package com.example.demo.Service.book;

import com.example.demo.Repository.book.BookRepo;
import com.example.demo.Repository.category.CategoryRepo;
import com.example.demo.Repository.tag.TagRepo;
import com.example.demo.entity.Category;
import com.example.demo.entity.Tag;
import com.example.demo.entity.book.Book;
import com.example.demo.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Transactional
@Service
public class BookUtils {

    private  final BookRepo bookRepo;

    private final TagRepo tagRepo;

    private final CategoryRepo categoryRepo;

    public Book findBookById(long id){
        return bookRepo.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("book", "id", id));
    }

    public Set<Tag> findSetTag(Set<Long> tags){
        Set<Tag> tagSet = new HashSet<>();
        for (Long tagId: tags
        ) {
            tagSet.add(tagRepo.findById(tagId).orElseThrow(
                    () -> new ResourceNotFoundException("tag", "id", tagId)
            ));
        }
        return tagSet;
    }

    public Set<Category> findSetCategory(Set<Long> categories){
        Set<Category> categorySet = new HashSet<>();
        for (Long categoryId: categories
        ) {
            categorySet.add(categoryRepo.findById(categoryId).orElseThrow(
                    () -> new ResourceNotFoundException("category", "id", categoryId)
            ));
        }
        return categorySet;
    }
}
