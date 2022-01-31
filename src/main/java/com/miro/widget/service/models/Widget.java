package com.miro.widget.service.models;

import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "widgets")
@AllArgsConstructor
@NoArgsConstructor
public class Widget {
    @Id
    @NonNull
    UUID id;

    @NonNull
    Integer z;

    @NonNull
    Integer centerX;

    @NonNull
    Integer centerY;

    @NonNull
    Integer width;

    @NonNull
    Integer height;

    @UpdateTimestamp
    ZonedDateTime updatedAt;
}

