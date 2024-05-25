package com.kill.gaebokchi.domain.bogu.entity;

import com.kill.gaebokchi.global.error.BadRequestException;
import com.kill.gaebokchi.global.error.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.thymeleaf.util.StringUtils;

@Getter
@RequiredArgsConstructor
public enum Type {
    TYPE1(0,0,0,"등교 복어"),
    TYPE2(1, 0,1,"열공 복어"),
    TYPE3(2, 1,0, "한복어 과장"),
    TYPE4(3, 1, 1, "강복어 대리"),
    TYPE5(4, 4, 1 , "김복어 사원"),
    TYPE6(5, 2, 0, "엄마 복어"),
    TYPE7(6, 2, 1, "아들 복어"),
    TYPE8(7, 2, 2, "딸 복어"),
    TYPE9(8, 2, 3, "아빠 복어"),
    TYPE10(9, 3, 0, "아싸 복어"),
    TYPE11(10, 3, 1, "연극 복어"),
    TYPE12(11, 3, 2, "혼밥 복어"),
    TYPE13(12, 3, 3, "마스크 복어"),
    TYPE14(13, 4, 0, "나홀로 복어"),
    TYPE15(14, 4, 1, "커플 복어"),
    TYPE16(15, 5, 0, "수액 복어"),
    TYPE17(16, 5, 1, "부상 복어"),
    TYPE18(17, 5, 2, "다침 복어"),
    TYPE23(18, 6, 0, "걱정 복어"),
    TYPE24(19, 7, 0, "고뇌 복어"),
    TYPE25(20, 7, 1, "사색 복어"),
    TYPE26(21, 8, 0, "화난 복어"),
    TYPE27(22, 8, 1, "눈물 복어");

    private final Integer id;
    private final Integer Category;
    private final Integer Variation;
    private final String name;

    public static String getNameFromCategoryAndVariation(Integer Category, Integer variation){
        for(Type type : Type.values()){
            if(type.getCategory()==Category && type.getVariation()==variation){
                return type.getName();
            }
        }
        throw new BadRequestException(ExceptionCode.NOT_FOUND_CATEGORY);
    }

    public static Integer getIdFromCategoryAndVariation(Integer Category, Integer variation){
        for(Type type : Type.values()){
            if(type.getCategory()==Category && type.getVariation()==variation){
                return type.getId();
            }
        }
        throw new BadRequestException(ExceptionCode.NOT_FOUND_CATEGORY);
    }

    public static Type fromId(Integer id){
        for(Type type : Type.values()){
            if(type.getId()==id) {
                return type;
            }
        }
        throw new BadRequestException(ExceptionCode.NOT_FOUND_DOGAM_BOGU_ID);
    }
}
