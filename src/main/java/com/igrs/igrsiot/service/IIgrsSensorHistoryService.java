package com.igrs.igrsiot.service;

import com.igrs.igrsiot.model.IgrsSensorHistory;

import java.util.List;

public interface IIgrsSensorHistoryService {
    List<IgrsSensorHistory> getDataByDateAndType(IgrsSensorHistory igrsSensorhistory);

    int insert(IgrsSensorHistory igrsSensorhistory);
}
