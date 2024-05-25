package com.kill.gaebokchi.domain.bogu.entity;

import com.kill.gaebokchi.global.error.BadRequestException;
import com.kill.gaebokchi.global.error.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.thymeleaf.util.StringUtils;

@Getter
@RequiredArgsConstructor
public enum Category {
    CATEGORY1(0,"학업"),
    CATEGORY2(1, "직장"),
    CATEGORY3(2, "가족"),
    CATEGORY4(3, "친구"),
    CATEGORY5(4, "연인"),
    CATEGORY6(5, "건강"),
    CATEGORY7(6, "사회문제"),
    CATEGORY8(7, "이유없음"),
    CATEGORY9(8, "기타");

    private final Integer id;
    private final String text;

    public static Category fromText(String text){
        for(Category category : Category.values()){
            if(StringUtils.equals(category.getText(),text)) {
                return category;
            }
        }
        throw new BadRequestException(ExceptionCode.NOT_FOUND_CATEGORY);
    }
    public static Category fromId(Integer id){
        for(Category category : Category.values()){
            if(category.getId()==id) {
                return category;
            }
        }
        throw new BadRequestException(ExceptionCode.NOT_FOUND_CATEGORY);
    }

}
