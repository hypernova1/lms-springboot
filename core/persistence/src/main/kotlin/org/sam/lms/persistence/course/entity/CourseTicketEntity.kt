package org.sam.lms.persistence.course.entity

import jakarta.persistence.*
import org.hibernate.annotations.Filter
import org.hibernate.annotations.SQLDelete
import org.sam.lms.domain.course.domain.CourseTicket
import org.sam.lms.persistence.AuditEntity
import java.time.LocalDateTime

@Filter(name = "deletedAccountFilter", condition = "deleted_date IS NULL OR :deletedDate = true")
@SQLDelete(sql = "UPDATE course_ticket SET deleted_date = current_timestamp WHERE id = ?")
@Table(name = "course_ticket")
@Entity
class CourseTicketEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, columnDefinition = "bigint")
    val studentId: Long,

    @Column(nullable = false, columnDefinition = "bigint")
    val courseId: Long,

    @Column(nullable = true, columnDefinition = "timestamp")
    val applicationDate: LocalDateTime = LocalDateTime.now(),
) : AuditEntity() {
    fun toDomain(): CourseTicket {
        return CourseTicket(id = this.id, courseId = this.courseId, studentId = this.studentId)
    }
}