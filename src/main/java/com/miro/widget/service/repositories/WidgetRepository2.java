package com.miro.widget.service.repositories;

import com.miro.widget.service.models.Widget2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface WidgetRepository2 extends CrudRepository<Widget2, UUID> {
    Optional<Widget2> findByZ(int z);

    Page<Widget2> findAll(Pageable pageable);
}
