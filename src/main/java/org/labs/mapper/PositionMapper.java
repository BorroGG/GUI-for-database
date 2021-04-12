package org.labs.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.labs.model.Equipment;
import org.labs.model.Position;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PositionMapper {
    @Insert("insert into position(positionid, tittle)" +
            "values(#{positionId}, #{tittle})")
    void save(Position position);

    @Select("select * from position")
    List<Position> selectAllPositions();

    @Select("select * from position where positionid = #{positionId}")
    Position selectPositionById(Position position);

    @Delete("delete from position where positionid = #{positionId}")
    void delete(Position position);

    @Update("update position set positionid = #{positionId}, tittle = #{tittle}" +
            "where positionid = #{positionId} ")
    void update(Position position);
}
