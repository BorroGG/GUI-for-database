package org.labs.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.labs.model.Equipment;
import org.labs.model.Mission;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MissionMapper {
    @Insert("insert into mission(name, description, begin_date, end_date, complete, author, executor, priorityid, customerid)" +
            "values(#{name}, #{description}, #{beginDate}, #{endDate}, #{complete}, #{author}, #{executor}, #{priorityId}, #{customerId})")
    void save(Mission mission);

    @Select("select * from mission")
    List<Mission> selectAllMissions();

    @Select("select * from mission where missionid = #{missionId}")
    Mission selectMissionById(Mission mission);

    @Select("select * from mission where executor = #{executor}")
    List<Mission> selectMissionByExecutor(Integer executor);

    @Delete("delete from mission where missionid = #{missionId}")
    void delete(Mission mission);

    @Update("update mission set missionid = #{missionId}, name = #{name}, description = #{description}," +
            " begin_date = #{beginDate}, end_date = #{endDate}, complete = #{complete}, author = #{author}, executor = #{executor}, priorityid = #{priorityId}, customerid = #{customerId}" +
            "where missionid = #{missionId} ")
    void update(Mission mission);
}
