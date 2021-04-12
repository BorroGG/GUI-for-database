package org.labs.mapper;

import org.labs.model.Contract;
import org.springframework.stereotype.Component;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Component
public interface ContractMapper {

    @Insert("insert into contract(number, dateconclusion, description, customerid)" +
            "values(#{number}, #{dateConclusion}, #{description},  #{customerId})")
    void save(Contract contract);

    @Select("select * from contract")
    List<Contract> selectAllContracts();

    @Select("select * from contract where number = #{number}")
    Contract selectContractByNumber(Contract contract);

    @Delete("delete from contract where number = #{number}")
    void delete(Contract contract);

    @Update("update contract set number = #{number}, dateconclusion = #{dateConclusion}, description = #{description}, customerid = #{customerId}" +
            "where number = #{number} ")
    void update(Contract contract);
}
