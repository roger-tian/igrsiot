package com.igrs.igrsiot.service.impl;

import com.igrs.igrsiot.dao.IIgrsSensorHistoryDao;
import com.igrs.igrsiot.model.IgrsSensorHistory;
import com.igrs.igrsiot.service.IIgrsSensorHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IgrsSensorHistoryServiceImpl implements IIgrsSensorHistoryService {
    @Override
    public List<IgrsSensorHistory> getDataByDateAndType(IgrsSensorHistory igrsSensorhistory) {
        return igrsSensorHistoryDao.getDataByDateAndType(igrsSensorhistory);
    }

    @Override
    public int insert(IgrsSensorHistory igrsSensorhistory) {
        return igrsSensorHistoryDao.insert(igrsSensorhistory);
    }

    @Autowired
    private IIgrsSensorHistoryDao igrsSensorHistoryDao;
}
