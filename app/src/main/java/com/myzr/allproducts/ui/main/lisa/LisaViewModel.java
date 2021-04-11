package com.myzr.allproducts.ui.main.lisa;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.myzr.allproducts.data.DemoRepository;
import com.myzr.allproducts.utils.lisatools.LisaTools;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * Created by goldze on 2017/7/17.
 */

public class LisaViewModel extends BaseViewModel <DemoRepository>{
    private static final String TAG="LoadingViewModel";
    public ObservableField<String> allDays = new ObservableField<>("2");
    public ObservableField<String> moreLeft = new ObservableField<>("0");
    public ObservableField<String> order = new ObservableField<>("a1,a1,ava_a8,a8,ava_com,com,com_com,com,cctc_com,com,com_opc,opc,cctc_opc,opc,ava_a8,a8,com");

    public ObservableField<String> topA1 = new ObservableField<>("2,1");
    public ObservableField<String> topA8 = new ObservableField<>("1,2");
    public ObservableField<String> topCom = new ObservableField<>("0,0");
    public ObservableField<String> topOpc = new ObservableField<>("0,0");

    public ObservableField<String> bottomA1 = new ObservableField<>("2,1");
    public ObservableField<String> bottomA8 = new ObservableField<>("1,2");
    public ObservableField<String> bottomCom = new ObservableField<>("0,0");
    public ObservableField<String> bottomOpc = new ObservableField<>("0,0");

    public ObservableField<String> intAva = new ObservableField<>("3,3");
    public ObservableField<String> intCom = new ObservableField<>("0,0");
    public ObservableField<String> intCctc = new ObservableField<>("0,0");

    public ObservableField<String> completeTaskStr = new ObservableField<>("3,3");
    public ObservableField<String> resultStr = new ObservableField<>("");
    public LisaViewModel(@NonNull Application application, DemoRepository repository) {
        super(application, repository);
    }
    public BindingCommand getResultOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if(TextUtils.isEmpty(completeTaskStr.get())){
                ToastUtils.showLong("输入的每一天需要完成的套数不能为空");
                return;
            }
            LisaTools.getInstance().initAllDays(Integer.parseInt(allDays.get()));
            LisaTools.getInstance().initOrder(order.get());
            LisaTools.getInstance().initMoreDatasForEveryDay(Integer.parseInt(moreLeft.get()));
            LisaTools.getInstance().initTopA1(topA1.get());
            LisaTools.getInstance().initTopA8(topA8.get());
            LisaTools.getInstance().initTopCom(topCom.get());
            LisaTools.getInstance().initTopOpc(topOpc.get());

            LisaTools.getInstance().initBottomA1(bottomA1.get());
            LisaTools.getInstance().initBottomA8(bottomA8.get());
            LisaTools.getInstance().initBottomCom(bottomCom.get());
            LisaTools.getInstance().initBottomOpc(bottomOpc.get());

            LisaTools.getInstance().initIntAva(intAva.get());
            LisaTools.getInstance().initIntCom(intCom.get());
            LisaTools.getInstance().initIntCctv(intCctc.get());

            resultStr.set(LisaTools.getInstance().judgeRules(completeTaskStr.get()));
        }
    });
}
