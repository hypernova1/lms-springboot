package org.sam.lms.persistence

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.ColumnDefault
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@MappedSuperclass
abstract class AuditEntity {

    @CreatedDate
    @ColumnDefault("current_timestamp")
    @Column(name = "created_date", nullable = false, columnDefinition = "timestamp")
    val createdDate: LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    @ColumnDefault("current_timestamp")
    @Column(name = "updated_date", nullable = false, columnDefinition = "timestamp")
    val updatedDate: LocalDateTime = LocalDateTime.now()

    @Column(name = "deleted_date", nullable = true)
    var deletedDate: LocalDateTime? = null


}