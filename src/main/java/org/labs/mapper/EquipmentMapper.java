package org.labs.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.labs.model.Equipment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface EquipmentMapper {
    @Insert("insert into equipment(sernumber, type, name, options, number)" +
            "values(#{serNumber}, #{type}, #{name}, #{options}, #{number})")
    void save(Equipment equipment);

    @Select("select * from equipment")
    List<Equipment> selectAllEquipments();

    @Select("select * from equipment where sernumber = #{serNumber}")
    Equipment selectEquipmentByNumber(Equipment equipment);

    @Delete("delete from equipment where sernumber = #{serNumber}")
    void delete(Equipment equipment);

    @Update("update equipment set sernumber = #{serNumber}, type = #{type}, options = #{options}," +
            " number = #{number}" +
            "where sernumber = #{serNumber} ")
    void update(Equipment equipment);
}
