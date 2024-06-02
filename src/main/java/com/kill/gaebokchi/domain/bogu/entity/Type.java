package com.kill.gaebokchi.domain.bogu.entity;

import com.kill.gaebokchi.global.error.BadRequestException;
import com.kill.gaebokchi.global.error.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.thymeleaf.util.StringUtils;

@Getter
public enum Type {
    TYPE1(0,0,0,"등교 복어", false),
    TYPE2(1, 0,1,"열공 복어", false),
    TYPE3(2, 1,0, "한복어 과장", false),
    TYPE4(3, 1, 1, "강복어 대리", false),
    TYPE5(4, 1, 2 , "김복어 사원", false),
    TYPE6(5, 2, 0, "엄마 복어", false),
    TYPE7(6, 2, 1, "아들 복어", false),
    TYPE8(7, 2, 2, "딸 복어", false),
    TYPE9(8, 2, 3, "아빠 복어", false),
    TYPE10(9, 3, 0, "아싸 복어", false),
    TYPE11(10, 3, 1, "연극 복어", false),
    TYPE12(11, 3, 2, "혼밥 복어", false),
    TYPE13(12, 3, 3, "마스크 복어", false),
    TYPE14(13, 4, 0, "나홀로 복어", false),
    TYPE15(14, 4, 1, "커플 복어", false),
    TYPE16(15, 5, 0, "수액 복어", false),
    TYPE17(16, 5, 1, "부상 복어", false),
    TYPE18(17, 5, 2, "다침 복어", false),
    TYPE23(18, 6, 0, "걱정 복어", false),
    TYPE24(19, 7, 0, "고뇌 복어", false),
    TYPE25(20, 7, 1, "사색 복어", false),
    TYPE26(21, 8, 0, "화난 복어", false),
    TYPE27(22, 8, 1, "눈물 복어", false);

    private Integer id;
    private Integer category;
    private Integer variation;
    private String name;
    private Boolean newFlag;

    Type(Integer id, Integer category, Integer variation, String name, Boolean newFlag) {
        this.id = id;
        this.category = category;
        this.variation = variation;
        this.name = name;
        this.newFlag = newFlag;
    }
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

    public static Type getTypeFromCategoryAndVariation(Integer Category, Integer variation){
        for(Type type : Type.values()){
            if(type.getCategory()==Category && type.getVariation()==variation){
                return type;
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

    public void setNewFlag(boolean newFlag){
        this.newFlag=newFlag;
    }
}
