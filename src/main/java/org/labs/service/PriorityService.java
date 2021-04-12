package org.labs.service;

import org.labs.mapper.PriorityMapper;
import org.labs.model.Position;
import org.labs.model.Priority;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriorityService {

    private final ModelMapper mapper = new ModelMapper();

    @Autowired
    private PriorityMapper priorityMapper;

    public void save(Priority saveDTO) {
        Priority priority = mapper.map(saveDTO, Priority.class);
        priorityMapper.save(priority);
    }

    public void update(Priority updateDTO) {
        Priority priority = mapper.map(updateDTO, Priority.class);
        priorityMapper.update(priority);
    }

    public List<Priority> selectAllPriorities() {
        return priorityMapper.selectAllPriorities();
    }

    public Priority selectPriorityById(Priority selectDTO) {
        Priority priority = mapper.map(selectDTO, Priority.class);
        return priorityMapper.selectPriorityById(priority);
    }

    public void delete(Priority deleteDTO) {
        Priority priority = mapper.map(deleteDTO, Priority.class);
        priorityMapper.delete(priority);
    }
}
