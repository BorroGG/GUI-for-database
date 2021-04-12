package org.labs.service;

import org.labs.mapper.PositionMapper;
import org.labs.model.Position;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionService {

    private final ModelMapper mapper = new ModelMapper();

    @Autowired
    private PositionMapper positionMapper;

    public void save(Position saveDTO) {
        Position position = mapper.map(saveDTO, Position.class);
        positionMapper.save(position);
    }

    public void update(Position updateDTO) {
        Position position = mapper.map(updateDTO, Position.class);
        positionMapper.update(position);
    }

    public List<Position> selectAllPositions() {
        return positionMapper.selectAllPositions();
    }

    public Position selectPositionById(Position selectDTO) {
        Position position = mapper.map(selectDTO, Position.class);
        return positionMapper.selectPositionById(position);
    }

    public void delete(Position deleteDTO) {
        Position position = mapper.map(deleteDTO, Position.class);
        positionMapper.delete(position);
    }
}
