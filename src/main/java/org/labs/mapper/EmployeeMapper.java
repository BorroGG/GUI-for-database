package org.labs.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.labs.model.Customer;
import org.labs.model.Employee;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface EmployeeMapper {

    @Insert("insert into employee(firstname, lastname, middlename, phone, email, fax, login, password, positionid)" +
            "values(#{firstName}, #{lastName}, #{middleName}, #{phone}, #{email}, #{fax}, #{login}, #{password}, #{positionId})")
    void saveEmployee(Employee employee);

    @Select("select * from employee")
    List<Employee> selectAllEmployees();

    @Select("select * from employee where login = #{login}")
    Employee selectEmployeeByLogin(Employee employee);

    @Select("select * from employee where servicenumber = #{serviceNumber}")
    Employee selectEmployeeByServiceNumber(Integer serviceNumber);

    @Delete("delete from employee where servicenumber = #{serviceNumber}")
    void delete(Employee employee);

    @Update("update employee set firstname = #{firstName}, lastname = #{lastName}," +
            " middlename = #{middleName}, phone = #{phone}, email = #{email}, fax = #{fax}, login = #{login}, password = #{password}, positionid = #{positionId}" +
            "where servicenumber = #{serviceNumber} ")
    void update(Employee employee);
}
