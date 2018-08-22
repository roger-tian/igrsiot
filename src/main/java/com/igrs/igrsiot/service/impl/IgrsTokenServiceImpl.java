package com.igrs.igrsiot.service.impl;

import com.alibaba.fastjson.JSONObject;
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
    public void updateExpired(IgrsToken igrsToken) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long timeStamp = new Date().getTime() + 1000 * 60 * 30;     // 30 seconds
        String expiredTime = df.format(timeStamp);

        igrsToken.setExpired(expiredTime);

        igrsTokenDao.updateExpired(igrsToken);
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

    public static boolean isTokenExpired(IgrsToken igrsToken) throws ParseException {
        if (igrsToken == null) {
            return false;
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = df.parse(df.format(new Date().getTime()));
        Date dateToken = df.parse(igrsToken.getExpired());
        if (date.after(dateToken)) {
            return true;
        }

        return false;
    }

    public static JSONObject genTokenErrorMsg(IgrsToken igrsToken) throws ParseException {
        JSONObject jsonResult = null;

        if (igrsToken == null) {
            jsonResult = new JSONObject();
            jsonResult.put("result", "FAIL");
            jsonResult.put("errCode", "401");
        } else if (isTokenExpired(igrsToken)) {
            jsonResult = new JSONObject();
            jsonResult.put("result", "FAIL");
            jsonResult.put("errCode", "402");
        }

        return jsonResult;
    }

    @Autowired
    private IIgrsTokenDao igrsTokenDao;

    private static final Logger logger = LoggerFactory.getLogger(IgrsTokenServiceImpl.class);
}
