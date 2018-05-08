package com.igrs.igrsiot.mapper;

import com.igrs.igrsiot.domain.IgrsOperate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperateMapper {
    void insertOperate(IgrsOperate operate);

    List<IgrsOperate> getOperates();

    String getOneOperate();
}
