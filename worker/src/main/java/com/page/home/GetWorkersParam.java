package com.page.home;

import com.framework.domain.param.BaseParam;

/**
 * Created by chenxi.cui on 2017/9/12.
 */

public class GetWorkersParam extends BaseParam {
    public int pageNo = 1;
    public int pageSize = 100;
    public String villageId;
}
