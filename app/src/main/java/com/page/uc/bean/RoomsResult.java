package com.page.uc.bean;

import com.framework.domain.response.BaseResult;

import java.util.List;

/**
 * Created by chenxi.cui on 2017/8/19.
 */

public class RoomsResult extends BaseResult {
    public RoomsData data;

    public static class RoomsData implements BaseData {
        public List<ComBean> datas;
    }
}
