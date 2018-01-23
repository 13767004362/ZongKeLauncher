package com.zongke.hapilolauncher.connection;

import android.content.Context;

import com.zongke.serviceconnection.client.DownloadServiceClient;
import com.zongke.serviceconnection.client.EngineServiceClient;
import com.zongke.serviceconnection.client.ImServiceClient;

/**
 * Created by ${xinGen} on 2017/12/28.
 * 远程服务管理类
 */

public class RemoteServicesClient {
    private DownloadServiceClient downloadServiceClient;
    private EngineServiceClient engineServiceClient;
    private ImServiceClient imServiceClient;
    private static RemoteServicesClient instance;
    private RemoteServicesClient(){
        this.downloadServiceClient=DownloadServiceClient.getInstance();
        this.engineServiceClient=EngineServiceClient.getInstance();
        this.imServiceClient=ImServiceClient.getInstance();
    }
    public static synchronized RemoteServicesClient getInstance(){
        if (instance==null){
            instance=new RemoteServicesClient();
        }
        return instance;
    }

    /**
     * 初始化
     * @param context
     */
    public void init(Context context){
        this.downloadServiceClient.init(context);
        this.engineServiceClient.init(context);
        this.imServiceClient.init(context);
    }

    /**
     * 开启对应的远程服务
     */
    public void startRemoteServices(){
        try {
            this.downloadServiceClient.startService();
            this.engineServiceClient.startService();
            this.imServiceClient.startService();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 绑定需要绑定的远程服务
     */
    public void bindRemoteServices(){
        try {
            this.downloadServiceClient.bindService();
            this.engineServiceClient.bindService();
            this.imServiceClient.bindService();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
