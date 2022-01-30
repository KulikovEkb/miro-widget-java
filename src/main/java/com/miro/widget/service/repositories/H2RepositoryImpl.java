package com.miro.widget.service.repositories;

import com.miro.widget.service.models.Widget2;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Profile("h2-repository")
public interface H2RepositoryImpl extends WidgetRepository2 {
    @Query(value = "SELECT * FROM widgets", nativeQuery = true, countQuery = "select count(id) from widgets")
    Page<Widget2> findAll(Pageable pageable);
}
