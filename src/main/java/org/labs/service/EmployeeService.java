package org.labs.service;

import org.labs.mapper.EmployeeMapper;
import org.labs.model.Employee;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final ModelMapper mapper = new ModelMapper();

    @Autowired
    private EmployeeMapper employeeMapper;

    public void save(Employee saveDTO) {
        Employee employee = mapper.map(saveDTO, Employee.class);
        employeeMapper.saveEmployee(employee);
    }

    public void update(Employee updateDTO) {
        Employee employee = mapper.map(updateDTO, Employee.class);
        employeeMapper.update(employee);
    }

    public List<Employee> selectAllEmployees() {
        return employeeMapper.selectAllEmployees();
    }

    public Employee selectEmployeeByLogin(Employee selectDTO) {
        Employee employee = mapper.map(selectDTO, Employee.class);
        return employeeMapper.selectEmployeeByLogin(employee);
    }

    public void delete(Employee deleteDTO) {
        Employee employee = mapper.map(deleteDTO, Employee.class);
        employeeMapper.delete(employee);
    }
}
