package com.igrs.igrsiot.service;

import com.igrs.igrsiot.model.IgrsOperate;
import com.igrs.igrsiot.dao.OperateMapper;
import org.springframework.stereotype.Service;

@Service
public class OperateService {
//    @Autowired
    private OperateMapper operateMapper;

    public void insertOperate(IgrsOperate operate) {
//        operateMapper.insertOperate(operate);
    }

//    public List<IgrsOperate> getOperates() {
//        return operateMapper.getOperates();
//    }

    public String getOneOperate() {
        return operateMapper.getOneOperate();
    }
}
