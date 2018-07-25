package com.igrs.igrsiot.service.impl;

import com.igrs.igrsiot.dao.IIgrsTokenDao;
import com.igrs.igrsiot.model.IgrsToken;
import com.igrs.igrsiot.service.IIgrsTokenService;
import com.igrs.igrsiot.utils.MD5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class IgrsTokenServiceImpl implements IIgrsTokenService {
    @Autowired
    private static IIgrsTokenDao igrsTokenDao;

    @Override
    public IgrsToken getTokenByUser(String user) {
        return igrsTokenDao.getTokenByUser(user);
    }

    @Override
    public IgrsToken getByToken(String token) {
        return igrsTokenDao.getByToken(token);
    }

    @Override
    public void updateToken(IgrsToken igrsToken) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long timeStamp = new Date().getTime() + 1000 * 60 * 30;     // 30 seconds
        String expiredTime = df.format(timeStamp);

        igrsToken.setExpired(expiredTime);

        igrsTokenDao.updateToken(igrsToken);
    }

    @Override
    public int updateExpired(IgrsToken igrsToken) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long timeStamp = new Date().getTime() + 1000 * 60 * 30;     // 30 seconds
        String expiredTime = df.format(timeStamp);

        igrsToken.setExpired(expiredTime);

        return igrsTokenDao.updateExpired(igrsToken);
    }

    @Override
    public void insert(IgrsToken igrsToken) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long timeStamp = new Date().getTime() + 1000 * 60 * 30;     // 30 seconds
        String expiredTime = df.format(timeStamp);

        igrsToken.setExpired(expiredTime);

        igrsTokenDao.insert(igrsToken);
    }

    public static String genToken(String user) {
        String token = user + "@" + new Date().getTime();
        logger.debug("token: {}", token);
        return MD5.md5(token);
    }

    public static Boolean isTokenExpired(String token) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = df.parse(df.format(new Date().getTime()));

        IgrsToken igrsToken = igrsTokenDao.getByToken(token);
        Date dateToken = df.parse(igrsToken.getExpired());
        if ((igrsToken != null) && (date.after(dateToken))) {
            return true;
        }

        return false;
    }

    private static final Logger logger = LoggerFactory.getLogger(IgrsTokenServiceImpl.class);
}