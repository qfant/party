package com.framework.net;


import com.framework.domain.response.BaseResult;
import com.framework.utils.Enums;
import com.page.detail.CameraDetailResult;
import com.page.detail.DetailResult;
import com.page.home.CamerasResult;
import com.page.home.NoticesResult;
import com.page.home.PersonsResult;
import com.page.home.TownsResult;
import com.page.login.activity.LoginResult;

/**
 * @author zexu
 */
public enum ServiceMap implements Enums.IType {
    getCameras("/Interface/getCameras", CamerasResult.class),
    getCameraDetail("/Interface/getCameraDetail", CameraDetailResult.class),
    getWorkers("/Interface/getWorkers", PersonsResult.class),
    getPersons("/Customer/person", PersonsResult.class),
    getNotices("/Interface/getNotices", NoticesResult.class),
    getTowns("/Customer/towns", TownsResult.class),
    checkVersion("/checkVersion.do", BaseResult.class),
    getVerificationCode("/getVerificationCode.do", BaseResult.class),
    customerLogin("/Customer/Login", LoginResult.class),
    getRepair("/getRepair.do", DetailResult.class),
    ;

    private final String mType;
    private final Class<? extends BaseResult> mClazz;
    private final int mTaskType;
    private final static int NET_TASK_START = 0;
    public final static int NET_TASKTYPE_CONTROL = NET_TASK_START;
    public final static int NET_TASKTYPE_FILE = NET_TASKTYPE_CONTROL + 1;
    public final static int NET_TASKTYPE_ALL = NET_TASKTYPE_FILE + 1;

    ServiceMap(String type, Class<? extends BaseResult> clazz) {
        this(type, clazz, NET_TASKTYPE_CONTROL);
    }

    ServiceMap(String type, Class<? extends BaseResult> clazz, int taskType) {
        this.mType = type;
        this.mClazz = clazz;
        this.mTaskType = taskType;
    }

    /**
     * 创建接口对应的resp的Result的对象
     *
     * @return AbsResult或其子类的对象
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseResult> T newResult() throws IllegalAccessException, InstantiationException {
        return (T) getClazz().newInstance();
    }

    @Override
    public String getDesc() {
        return mType;
    }

    public Class<? extends BaseResult> getClazz() {
        return mClazz;
    }

    @Override
    public int getCode() {
        return mTaskType;
    }
}
