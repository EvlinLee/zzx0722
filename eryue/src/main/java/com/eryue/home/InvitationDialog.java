package com.eryue.home;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.eryue.AccountUtil;
import com.eryue.BaseApplication;
import com.eryue.R;
import com.library.util.StringUtils;
import com.library.util.ToastTools;

import net.DataCenterManager;
import net.InterfaceManager;
import net.NetManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static net.InterfaceManager.baseURL;
import static net.KeyFlag.BASE_IP_KEY_INPUT;
import static net.KeyFlag.BASE_IP_VALUE_KEY_INPUT;
import static net.KeyFlag.INVITE_CODE_KEY_INPUT;
import static net.KeyFlag.NORMAL_UID_KEY;

/**
 * 邀请码
 * Created by enjoy on 2018/3/10.
 */

public class InvitationDialog extends DialogFragment implements View.OnClickListener{

    /**
     * 邀请码编辑框
     */
    private EditText et_code;

    /**
     * 确定
     */
    private TextView tv_ok;

    private ImageView iv_close;

    private HomeRequestPresenter presenter;

    private String baseIP = AccountUtil.getBaseIp();
    private long uid = StringUtils.valueOfLong(AccountUtil.getUID());

    @Override
    public void onClick(View v) {
        if (v == tv_ok){


            //确定
            //初始化逻辑
            String code = et_code.getText().toString();
            if (TextUtils.isEmpty(code)){
                ToastTools.showShort(getContext(),"邀请码不能为空");
                return;
            }
            //登录后初始化baseip，激活码等信息
            getIpByCode(code);
        }else if (v == iv_close){

            //关闭
            ((Activity)getContext()).finish();
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_DialogWhenLarge_NoActionBar);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
    }
    private void autoFocus() {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View contentView = inflater.inflate(R.layout.view_invitation, null);

//        window.setLayout(-1, -2);//这2行,和上面的一样,注意顺序就行;
        //邀请码
        et_code = (EditText) contentView.findViewById(R.id.et_code);
        tv_ok = (TextView) contentView.findViewById(R.id.tv_ok);
        iv_close = (ImageView) contentView.findViewById(R.id.iv_close);

        tv_ok.setOnClickListener(this);
        iv_close.setOnClickListener(this);

        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (null!=onPopListener){
                    onPopListener.onDismiss();
                }
            }
        });
        getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (null!=onPopListener){
                    onPopListener.onDismiss();
                }
            }
        });
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK){
                    if (null!=onPopListener){
                        onPopListener.onDismiss();
                    }
                    return true;

                }
                return false;
            }
        });
        final Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处

        return contentView;
    }


    // 邀请码获取IP接口
    private void getIpByCode(final String code) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.GetIpByInvitationCode getIpByInvitationCode = retrofit.create(InterfaceManager.GetIpByInvitationCode.class);
        Call<InterfaceManager.IpResponse> call = getIpByInvitationCode.get(code);
        call.enqueue(new Callback<InterfaceManager.IpResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.IpResponse> call, Response<InterfaceManager.IpResponse> response) {
                if (null!=response.body()&&response.body().status == 1) {
                    if (response.body().result != null && response.body().result.size() > 0) {
                        InterfaceManager.IpResponse.IpInfo ipInfo = response.body().result.get(0);
                        String baseIP = "http://" + ipInfo.ip + "/";
                        long uid = ipInfo.uid;
                        DataCenterManager.Instance().save(getContext(), NORMAL_UID_KEY, uid + "");
                        DataCenterManager.Instance().save(getContext(), BASE_IP_KEY_INPUT, baseIP);
                        DataCenterManager.Instance().save(getContext(), BASE_IP_VALUE_KEY_INPUT, ipInfo.ip);

                        if (!TextUtils.isEmpty(code)) {
                            //全局邀请码赋值
                            DataCenterManager.Instance().save(getContext(), INVITE_CODE_KEY_INPUT, code);
                        }


                        NetManager.getInstance().AddActivator();

                        //获取domain
                        if(null == presenter){
                            presenter = new HomeRequestPresenter();
                        }
                        presenter.getServerConfig(getContext());

                        if (null!=onPopListener){
                            onPopListener.onSuccess();
                        }
                        InvitationDialog.this.dismiss();

                    }
                } else if (null!=response.body()&&response.body().status == 0) {
//                    ToastTools.showShort(getContext(), "邀请码错误，请重新输入");
                    showErrorDialog();
                } else {
                    ToastTools.showShort(getContext(), "未知错误");
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.IpResponse> call, Throwable t) {
                t.printStackTrace();
                ToastTools.showShort(getContext(), "网络异常，请稍后重试");
            }
        });
    }


    OnPopListener onPopListener;

    public void setOnPopListener(OnPopListener onPopListener) {
        this.onPopListener = onPopListener;
    }

    public interface OnPopListener{
        void onDismiss();

        void onSuccess();

    }


    AlertDialog.Builder normalDialog;
    private void showErrorDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        if (null==normalDialog){
            normalDialog =
                    new AlertDialog.Builder(getContext());
            normalDialog.setTitle("提示");
            normalDialog.setMessage("邀请码错误，请重新输入");
            normalDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(null!=et_code){
                                et_code.setText("");
                            }
                        }
                    });
        }
        // 显示
        normalDialog.show();
    }





}
