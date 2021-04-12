package org.labs.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.labs.model.Contract;
import org.labs.model.Customer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CustomerMapper {

    @Insert("insert into customer(customerid, firstname, lastname, middlename, phone, email, inn)" +
            "values(#{customerId}, #{firstName}, #{lastName}, #{middleName}, #{phone}, #{email}, #{inn})")
    void save(Customer customer);

    @Select("select * from customer")
    List<Customer> selectAllCustomers();

    @Select("select * from customer where customerid = #{customerId}")
    Customer selectCustomerById(Customer customer);

    @Delete("delete from customer where customerid = #{customerId}")
    void delete(Customer customer);

    @Update("update customer set customerid = #{customerId}, firstname = #{firstName}, lastname = #{lastName}," +
            " middlename = #{middleName}, phone = #{phone}, email = #{email}, inn = #{inn}" +
            "where customerid = #{customerId} ")
    void update(Customer customer);
}
