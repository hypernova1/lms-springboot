package org.sam.lms.course.domain

import org.sam.lms.course.infrastructure.persistence.entity.CourseTicketEntity

interface CourseTicketRepository {
    fun existsByCourseId(courseId: Long): Boolean
    fun existsByStudentId(studentId: Long): Boolean
    fun save(courseTicketEntity: CourseTicketEntity): CourseTicketEntity
}