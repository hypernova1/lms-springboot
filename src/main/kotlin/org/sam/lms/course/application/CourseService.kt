package org.sam.lms.course.application

import jakarta.transaction.Transactional
import org.sam.lms.common.exception.BadRequestException
import org.sam.lms.common.exception.ConflictException
import org.sam.lms.common.exception.ErrorCode
import org.sam.lms.course.application.payload.`in`.CreateCourseDto
import org.sam.lms.course.application.payload.`in`.UpdateCourseDto
import org.sam.lms.course.application.payload.out.CourseSummary
import org.sam.lms.course.domain.*
import org.springframework.stereotype.Service

@Service
class CourseService(
    private val courseReader: CourseReader,
    private val courseProcessor: CourseProcessor,
    private val categoryReader: CategoryReader,
    private val courseTicketReader: CourseTicketReader,
    private val courseTicketProcessor: CourseTicketProcessor,
) {

    /**
     * 강의 정보를 등록한다.
     *
     * @param createCourseDto 강의 정보
     * @param accountId 등록자 아이디
     * @return 강의 요약 정보
     * */
    @Transactional
    fun create(createCourseDto: CreateCourseDto, accountId: Long): CourseSummary {
        val category = this.categoryReader.findOne(createCourseDto.categoryId)
        val course = Course.of(createCourseDto, category, accountId);
        this.courseProcessor.save(course)
        return CourseSummary(id = course.id, title = course.title)
    }

    /**
     * 강의 정보를 수정한다.
     *
     * @param updateCourseDto 수정 정보
     * @param accountId
     * */
    @Transactional
    fun update(updateCourseDto: UpdateCourseDto, accountId: Long): CourseSummary {
        val course = this.courseReader.findOne(updateCourseDto.id)
        val category = this.categoryReader.findOne(updateCourseDto.categoryId)
        course.update(updateCourseDto, category, accountId)
        this.courseProcessor.save(course)
        return CourseSummary(id = course.id, title = course.title)
    }

    /**
     * 강의를 오픈한다.
     * */
    @Transactional
    fun open(id: Long, accountId: Long) {
        val course = this.courseReader.findOne(id)
        course.open(accountId)
    }

    /**
     * 강의를 삭제한다. 수강 중인 원생이 있을 경우 삭제할 수 없다.
     * */
    @Transactional
    fun delete(id: Long, accountId: Long) {
        val course = this.courseReader.findOne(id)
        course.checkUpdatePermission(accountId)
        val existTickets = this.courseTicketReader.existsByCourseId(id)
        if (existTickets) {
            throw BadRequestException(ErrorCode.EXISTS_STUDENTS)
        }

        this.courseProcessor.delete(id)
    }

    /**
     * 수강 신청을 한다. 이미 수강 중인 강의는 신청할 수 없다.
     *
     * @param id 강의 아이디
     * @param accountId 수강할 학생 아이디
     * */
    @Transactional
    fun enroll(id: Long, accountId: Long) {
        val course = this.courseReader.findOne(id)
        val existsStudent = this.courseTicketReader.existsByStudentId(accountId)
        if (existsStudent) {
            throw ConflictException(ErrorCode.ALREADY_JOINED_STUDENT)
        }
        println(course)
        println(course.category)
        val courseTicket = course.enroll(accountId)

        this.courseTicketProcessor.save(courseTicket)
        this.courseProcessor.save(course)
    }
}