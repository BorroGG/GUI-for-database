package org.labs.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.labs.model.Position;
import org.labs.model.Priority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PriorityMapper {

    @Insert("insert into priority(priorityid, name)" +
            "values(#{priorityId}, #{name})")
    void save(Priority priority);

    @Select("select * from priority")
    List<Priority> selectAllPriorities();

    @Select("select * from priority where priorityid = #{priorityId}")
    Priority selectPriorityById(Priority priority);

    @Delete("delete from priority where priorityid = #{priorityId}")
    void delete(Priority priority);

    @Update("update priority set priorityid = #{priorityId}, name = #{name}" +
            "where priorityid = #{priorityId} ")
    void update(Priority priority);
}
