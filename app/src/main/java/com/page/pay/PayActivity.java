package com.page.pay;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.framework.activity.BaseActivity;
import com.framework.app.AppConstants;
import com.framework.net.NetworkParam;
import com.framework.net.Request;
import com.framework.net.ServiceMap;
import com.page.home.activity.MainActivity;
import com.page.store.payresult.activity.PayResultActivity;
import com.qfant.wuye.R;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chenxi.cui on 2017/9/11.
 */

public class PayActivity extends BaseActivity {
    protected static final int SDK_PAY_FLAG = 0x35;
    @BindView(R.id.btn_pay)
    Button btnPay;
    @BindView(R.id.text_price)
    TextView textPrice;
    @BindView(R.id.image_pay_ari)
    ImageView imagePayAri;
    @BindView(R.id.ll_pay_ari)
    LinearLayout llPayAri;
    @BindView(R.id.image_pay_wechat)
    ImageView imagePayWechat;
    @BindView(R.id.ll_pay_wechat)
    LinearLayout llPayWechat;
    private ProductPayResult payResult;
    private int payType;
    private PayData order;
    private int form;
    private PayParam payParam;
    private WeChatPayResult weChatPayResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pub_pay_activity);
        setTitleBar("支付方式", true);
        ButterKnife.bind(this);
        order = (PayData) myBundle.getSerializable("order");
        if (order == null) {
            return;
        }
        form = order.from;
        payParam = new PayParam();
        payParam.orderid = order.id;
        payParam.price = order.price;

        llPayAri.performClick();
        textPrice.setText(String.format("总共支付:%s元", order.price));
    }

    @Override
    public void onNewIntent(Intent intent) {
        int resp = intent.getIntExtra("resp", -1);
        if (resp == 0) {
            goOrderDetail();
        } else if (resp == -2) {
            showToast("取消支付");
        }else if (resp == -1) {
            showToast("支付失败");
        }
        super.onNewIntent(intent);
    }

    @Override
    protected Handler.Callback genCallback() {

        return new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case SDK_PAY_FLAG: {
                        PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                        /**
                         对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                         */
                        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                        String resultStatus = payResult.getResultStatus();
                        // 判断resultStatus 为9000则代表支付成功
                        if (TextUtils.equals(resultStatus, "9000")) {
                            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                            showToast("支付成功");
                            goOrderDetail();
                        } else {
                            // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                            showToast("支付失败");
                        }
                        break;
                    }

                }
                return false;
            }
        };
    }

    /**
     * 支付宝支付
     *
     * @param
     * */


    public void airPay(final String payInfo) {

        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PayActivity.this);
                Map<String, String> result = alipay.payV2(payInfo, true);
                Log.i("msp", result.toString());
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private void weChatPay(WeChatPayResult weChatPayResult) {
        //B589DE43332F641300836F48AABB9B9E
        //7FFECB600D7157C5AA49810D2D8F28BC2811827B
        IWXAPI api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp(AppConstants.APP_ID);
        PayReq request = new PayReq();
        request.appId = AppConstants.APP_ID;
        request.partnerId = weChatPayResult.data.partnerid;
        request.prepayId = weChatPayResult.data.prepayid;
        request.packageValue =weChatPayResult.data.packageStr;
        request.nonceStr =weChatPayResult.data.nonce_str;
        request.timeStamp =weChatPayResult.data.timestamp;
        request.sign = weChatPayResult.data.sign;
        api.sendReq(request);
    }

    private void goOrderDetail() {
        if (form == -1) {
            qBackToActivity(MainActivity.class, null);
        }else {
            Bundle bundle = new Bundle();
            bundle.putString("id", order.id + "");
            bundle.putString("orderno", order.orderno + "");
            qBackToActivity(PayResultActivity.class, bundle);
        }
        finish();
    }

    @Override
    public boolean onMsgSearchComplete(NetworkParam param) {
        if (param.key == ServiceMap.alipayPayProduct || param.key == ServiceMap.alipayPayWuyeFee) {
            if (param.result.bstatus.code == 0) {
                payResult = (ProductPayResult) param.result;
                airPay(payResult.data.params);
            }
        } else  if (param.key == ServiceMap.wechatPayProduct || param.key == ServiceMap.wechatPayWuyeFee) {
            if (param.result.bstatus.code == 0) {
                weChatPayResult = (WeChatPayResult) param.result;
                weChatPay(weChatPayResult);
            }
        }
        return super.onMsgSearchComplete(param);
    }

    @OnClick(R.id.btn_pay)
    public void onClick() {
        if (payType == 0) {
            if (form == -1) {
                Request.startRequest(payParam, ServiceMap.alipayPayWuyeFee, mHandler, Request.RequestFeature.BLOCK);
            } else {
                Request.startRequest(payParam, ServiceMap.alipayPayProduct, mHandler, Request.RequestFeature.BLOCK);
            }
        } else {
            if (form == -1) {
                Request.startRequest(payParam, ServiceMap.wechatPayWuyeFee, mHandler, Request.RequestFeature.BLOCK);
            } else {
                Request.startRequest(payParam, ServiceMap.wechatPayProduct, mHandler, Request.RequestFeature.BLOCK);
            }
        }
    }


    @OnClick({R.id.ll_pay_ari, R.id.ll_pay_wechat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_pay_ari:
                imagePayAri.setImageResource(R.drawable.ext_switch_circle_select);
                imagePayWechat.setImageResource(R.drawable.ext_switch_circle);
                payType = 0;
                break;
            case R.id.ll_pay_wechat:
                payType = 1;
                imagePayWechat.setImageResource(R.drawable.ext_switch_circle_select);
                imagePayAri.setImageResource(R.drawable.ext_switch_circle);
                break;
        }
    }
}
