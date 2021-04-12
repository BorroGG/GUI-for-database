package org.labs.service;

import org.labs.mapper.MissionMapper;
import org.labs.model.Employee;
import org.labs.model.Mission;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MissionService {

    private final ModelMapper mapper = new ModelMapper();

    @Autowired
    private MissionMapper missionMapper;

    public void save(Mission saveDTO) {
        Mission mission = mapper.map(saveDTO, Mission.class);
        missionMapper.save(mission);
    }

    public void update(Mission updateDTO) {
        Mission mission = mapper.map(updateDTO, Mission.class);
        missionMapper.update(mission);
    }

    public List<Mission> selectAllMissions() {
        return missionMapper.selectAllMissions();
    }

    public Mission selectMissionByNumber(Mission selectDTO) {
        Mission mission = mapper.map(selectDTO, Mission.class);
        return missionMapper.selectMissionByNumber(mission);
    }

    public void delete(Mission deleteDTO) {
        Mission mission = mapper.map(deleteDTO, Mission.class);
        missionMapper.delete(mission);
    }
}
