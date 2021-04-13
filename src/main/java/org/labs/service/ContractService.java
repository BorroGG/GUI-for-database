package org.labs.service;

import org.labs.mapper.ContractMapper;
import org.labs.model.Contract;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractService {

    private final ModelMapper mapper = new ModelMapper();

    @Autowired
    private ContractMapper contractMapper;

    public void save(Contract saveDTO) {
        Contract contract = mapper.map(saveDTO, Contract.class);
        contractMapper.save(contract);
    }

    public void update(Contract updateDTO) {
        Contract contract = mapper.map(updateDTO, Contract.class);
        contractMapper.update(contract);
    }

    public List<Contract> selectAllContracts() {
        return contractMapper.selectAllContracts();
    }

    public Contract selectContractByNumber(Contract selectDTO) {
        Contract contract = mapper.map(selectDTO, Contract.class);
        return contractMapper.selectContractByNumber(contract);
    }

    public void delete(Contract deleteDTO) {
        Contract contract = mapper.map(deleteDTO, Contract.class);
        contractMapper.delete(contract);
    }
}
