package com.changcai.buyer.im.main.fragment;


import com.changcai.buyer.R;
import com.changcai.buyer.im.main.model.MainTab;

/**
 * 聊天室主TAB页
 * Created by huangjun on 2015/12/11.
 */
public class ChatRoomListFragment extends MainTabFragment {
    private ChatRoomListFragment fragment;

    public ChatRoomListFragment() {
        setContainerId(MainTab.CHAT_ROOM.fragmentId);
    }

    @Override
    protected void onInit() {
        // 采用静态集成，这里不需要做什么了
        fragment = (ChatRoomListFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.chat_rooms_fragment);
    }

    @Override
    public void onCurrent() {
        super.onCurrent();
        if (fragment != null) {
            fragment.onCurrent();
        }
    }
}
