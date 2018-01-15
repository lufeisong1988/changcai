import android.os.Environment;

import com.changcai.buyer.util.LogUtil;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by lufeisong on 2018/1/11.
 */

public class TestDeleteContacts {
    private DeleteCallback callback;
    public TestDeleteContacts(DeleteCallback callback) {
        this.callback = callback;

    }

    public void login(final String account, String token) {
//        if(!NimUIKit.getAccount().isEmpty()){
//            LogUtil.d("NimIM", "上个账户未退出登录");
//            NimUIKit.logout();
//        }
        LogUtil.d("NimIM", "login " + account  );
        final LoginInfo loginInfo = new LoginInfo(account, token);
        NimUIKit.login(loginInfo, new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo loginInfo) {
                LogUtil.d("NimIM", "account = " + account + " login onSuccess");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getRecentContacts(loginInfo.getAccount(),loginInfo.getToken());
            }

            @Override
            public void onFailed(int i) {
                try {
                    charOutStream("{'accid':'" + loginInfo.getAccount() +"','imToken':'"+ loginInfo.getToken() + "'},",false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                callback.deleteFail();
                LogUtil.d("NimIM", "account = " + account + " login onFailed i = " + i);
            }

            @Override
            public void onException(Throwable throwable) {
                try {
                    charOutStream("{'accid':'" + loginInfo.getAccount() +"','imToken':'"+ loginInfo.getToken() + "'},",false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                callback.deleteFail();
                LogUtil.d("NimIM", "login onException throwable = " + throwable.toString());
            }
        });
    }

    private void getRecentContacts(final String account, final String token) {
        // 查询最近联系人列表数据
        NIMClient
                .getService(MsgService.class)
                .queryRecentContacts()
                .setCallback(new RequestCallbackWrapper<List<RecentContact>>() {

                                 @Override
                                 public void onResult(int code, List<RecentContact> recents, Throwable exception) {
                                     if (code != ResponseCode.RES_SUCCESS || recents == null) {
                                         LogUtil.d("NimIM", "account = " + account + " return");
                                         try {
                                             charOutStream("{'accid':'" + account +"','imToken':'"+ token + "'},",false);
                                         } catch (Exception e) {
                                             e.printStackTrace();
                                         }
                                         NimUIKit.logout();
                                         callback.deleteFail();
                                         return;
                                     }
                                     LogUtil.d("NimIM", "account = " + account + " recents.size = " + recents.size() + "================================================");
                                     if(recents.size() == 0){

                                         try {
                                             charOutStream("{'accid':'" + account +"','imToken':'"+ token + "'},",true);
                                         } catch (Exception e) {
                                             e.printStackTrace();
                                         }
                                         NimUIKit.logout();
                                         callback.deleteSucceed();
                                     }else{
                                         for (RecentContact recentContact : recents) {
                                             NIMClient.getService(MsgService.class).deleteRoamingRecentContact(recentContact.getContactId(), recentContact.getSessionType());
                                             LogUtil.d("NimIM", "account = " + account + " delete contact : contactId = " + recentContact.getContactId() + " ; sessionType = " + recentContact.getSessionType());
                                         }
                                         try {
                                             charOutStream("{'accid':'" + account +"','imToken':'"+ token + "'},",true);
                                         } catch (Exception e) {
                                             LogUtil.d("NimIM","e = " + e.toString());
                                             e.printStackTrace();
                                         }
                                         NimUIKit.logout();
                                         callback.deleteSucceed();
                                     }

                                 }
                             }

                );
    }
    public interface DeleteCallback{
        void deleteFail();
        void deleteSucceed();
    }

    public void charOutStream(String str,boolean clearSucceed) throws Exception{

        // 1：利用File类找到要操作的对象
        String path = "";
        if(clearSucceed){
            path = "网易云信清理聊天记录账号.txt";
        }else{
            path = "网易云信未清理聊天记录账号.txt";

        }
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + path);
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        //2：准备输出流
        FileOutputStream fileOutputStream = new FileOutputStream(file,true);
        byte[] bytes = str.getBytes();
        fileOutputStream.write(bytes);
        fileOutputStream.close();
//        if(clearSucceed){
//            callback.deleteSucceed();
//        }else{
//            callback.deleteFail();
//        }
    }
    public void readStream(String path) throws Exception{
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + path);
        FileInputStream fileInputStream = new FileInputStream(file);
        int length = fileInputStream.available();
        byte[] bytes = new byte[length];
        fileInputStream.read(bytes);
        String res = new String(bytes);
        LogUtil.d("NimIM","res = " + res);
    }
}
