package com.igrs.igrsiot.dao;

import com.igrs.igrsiot.model.IgrsSensorHistory;

import java.util.List;

public interface IIgrsSensorHistoryDao {
    List<IgrsSensorHistory> getDataByDateAndType(IgrsSensorHistory igrsSensorhistory);

    int insert(IgrsSensorHistory igrsSensorhistory);
}