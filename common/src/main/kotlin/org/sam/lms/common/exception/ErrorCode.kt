package org.sam.lms.common.exception

enum class ErrorCode(val code: String, val message: String) {
    // 유저
    ALREADY_EXISTED_USER_ID("1000", "이미 사용되고 있는 아이디입니다."),
    UNABLE_TO_REGISTER_USER("1001", "가입할 수 없는 유저입니다."),
    ACCOUNT_NOT_FOUND("1002", "유저를 찾을 수 없습니다."),

    // 권한
    ACCOUNT_MISMATCH("2000", "계정 정보가 일치하지 않습니다."),
    UNAUTHORIZED_TOKEN("2001", "토큰 정보가 유효하지 않습니다."),
    ROLE_NAME_NOT_FOUND("2002", "권한 정보가 없습니다."),

    // 카테고리
    CATEGORY_NOT_FOUND("3000", "카테고리가 존재하지 않습니다."),
    COURSE_NOT_FOUND("3001", "강의가 존재하지 않습니다."),
    ALREADY_VISIBLE("3002", "이미 오픈된 강의입니다."),
    EXISTS_STUDENTS("3003", "수강 중인 학생이 존재합니다."),

    // 수강생
    ALREADY_JOINED_STUDENT("4000,", "이미 수강 중입니다."),


    MISMATCH_PARAMETER("9999", ""),
    ADDRESS_NOT_FOUND("", "주소를 찾을 수 없습니다."),
    ENROLLMENT_FULL("", "더 이상 수강 할 수 없는 강의입니다."),
    ORDER_NOT_FOUND("", "주문을 찾을 수 없습니다."),
    COURSE_FULL("", "수강 가능한 인원이 마감되었습니다."),
    ALREADY_PAID("", "잘못된 요청입니다. 결제 성공 후 실패할 수 없습니다."),
    NOT_WAITING_PAYMENT("", "대기 중이 아닌 결제는 결제 완료할 수 없습니다."),
    PAYMENT_TIME_OUT("", "결제 가능 시간이 초과되었습니다."),
    ALREADY_ADDED_CART_ITEM("", "이미 추가된 강좌입니다."),
    CART_EMPTY("", "장바구니가 비어있습니다."),
    COURSE_TICKET_NOT_FOUND("", "수강권 정보를 찾을 수 없습니다.")

}