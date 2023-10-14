package com.coolSchool.CoolSchool.repositories;

import com.coolSchool.CoolSchool.models.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findByDeletedFalse();

    Optional<Blog> findByIdAndDeletedFalse(Long id);

    @Query("SELECT b FROM Blog b WHERE b.deleted = false ORDER BY b.created_at DESC")
    List<Blog> findAllByNewestFirst();

    @Query("SELECT b FROM Blog b WHERE b.deleted = false ORDER BY SIZE(b.liked_users) DESC")
    List<Blog> findAllByMostLiked();

    @Query("SELECT b FROM Blog b WHERE LOWER(b.title) LIKE %:keyword% AND b.deleted = false")
    List<Blog> searchByTitleContainingIgnoreCase(@Param("keyword") String keyword);

    @Query("SELECT b FROM Blog b WHERE LOWER(b.summary) LIKE %:keyword% AND b.deleted = false")
    List<Blog> searchBySummaryContainingIgnoreCase(@Param("keyword") String keyword);
}