package com.example.weibo.service.impl;

import com.example.common.resp.Const;
import com.example.common.resp.RestResponse;
import com.example.weibo.dao.WeiboHandler;
import com.example.weibo.service.WeiboService;
import com.example.weibo.vo.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "weiboServiceImpl")
public class WeiboServiceImpl implements WeiboService {

    @Autowired
    private WeiboHandler weiboHandler;

    @Override
    public RestResponse<String> postWeibo(String status, String username) {
        String mess = weiboHandler.postWeibo(status, username);
        if(Const.POST_SUCC.equals(mess)) {
            return RestResponse.createBySuccMessage(mess);
        }
        return RestResponse.createByError();
    }

    @Override
    public List<Status> getUserWeibo(String username) {
        List<Status> statuses = weiboHandler.getStatuses(username);
        return statuses;
    }


}
