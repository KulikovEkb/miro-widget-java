package com.miro.widget.service.repositories;

import com.miro.widget.service.models.Widget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface WidgetRepository extends CrudRepository<Widget, UUID> {
    Optional<Widget> findByZ(int z);

    Page<Widget> findAll(Pageable pageable);
}
