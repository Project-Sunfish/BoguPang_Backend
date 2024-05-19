package com.kill.gaebokchi.domain.bogu.entity;

import com.kill.gaebokchi.global.error.BadRequestException;
import com.kill.gaebokchi.global.error.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.thymeleaf.util.StringUtils;

@Getter
@RequiredArgsConstructor
public enum Type {
    TYPE1(0,0,"등교 복어"),
    TYPE2(0,1,"열공 복어"),
    TYPE3(0,2,"미정 복어"),
    TYPE4(0,3, "미정 복어"),
    TYPE5(1,0, "한복어 과장"),
    TYPE6(1, 1, "강복어 대리"),
    TYPE7(1, 2, "김복어 사원"),
    TYPE8(1, 3, "미정 복어"),
    TYPE9(2, 0, "엄마 복어"),
    TYPE10(2, 1, "아들 복어"),
    TYPE11(2, 2, "딸 복어"),
    TYPE12(2, 3, "아빠 복어"),
    TYPE13(3, 0, "아싸 복어"),
    TYPE14(3, 1, "연극 복어"),
    TYPE15(3, 2, "혼밥 복어"),
    TYPE16(3, 3, "미정 복어"),
    TYPE17(4, 0, "나홀로 복어"),
    TYPE18(4, 1, "커플 복어"),
    TYPE19(5, 0, "수액 복어"),
    TYPE20(5, 1, "부상 복어"),
    TYPE21(5, 2, "다침 복어"),
    TYPE22(5, 3, "미정 복어"),
    TYPE23(6, 0, "걱정 복어"),
    TYPE24(7, 0, "고뇌 복어"),
    TYPE25(7, 1, "사색 복어"),
    TYPE26(8, 0, "미정 복어"),
    TYPE27(8, 1, "미정 복어"),
    TYPE28(8, 2, "미정 복어");

    private final Integer Category;
    private final Integer Variation;
    private final String name;

    public static String fromCategoryAndVariation(Integer Category, Integer variation){
        for(Type type : Type.values()){
            if(type.getCategory()==Category && type.getVariation()==variation){
                return type.getName();
            }
        }
        throw new BadRequestException(ExceptionCode.NOT_FOUND_CATEGORY);
    }
}
