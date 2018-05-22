package com.igrs.igrsiot.service.impl;

import com.igrs.igrsiot.dao.IIgrsOperateDao;
import com.igrs.igrsiot.model.IgrsOperate;
import com.igrs.igrsiot.service.IIgrsOperateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IgrsOperateServiceImpl implements IIgrsOperateService {
    @Autowired
    IIgrsOperateDao igrsOperateDao;

    @Override
    public List<IgrsOperate> getAllOperates() {
        return igrsOperateDao.getAllOperates();
    }

    @Override
    public int insert(IgrsOperate record) {
        return igrsOperateDao.insert(record);
    }
}
