package com.miro.widget.service.repositories;

import com.miro.widget.service.models.Widget;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Profile("h2-repository")
public interface H2RepositoryImpl extends WidgetRepository {
    @Query(
        value = "SELECT * FROM widgets ORDER BY z",
        nativeQuery = true,
        countQuery = "select count(id) from widgets")
    Page<Widget> findAll(Pageable pageable);
}
