package com.changcai.buyer.listener;

import com.squareup.picasso.Picasso;

/**
 * Created by liuxingwei on 16/9/6.
 */
public abstract class PicassoPauseOnScrollListener extends PauseOnScrollListener {
    public final boolean pauseOnScroll;
    public final boolean pauseOnFling;

    public PicassoPauseOnScrollListener(boolean pauseOnScroll, boolean pauseOnFling) {
        this.pauseOnFling = pauseOnFling;
        this.pauseOnScroll = pauseOnScroll;
    }
//
//    @Override
//    public void resume() {
//        Picasso.with(getActivity()).resumeTag(getActivity());
//    }
//
//    @Override
//    public void pause() {
//        Picasso.with(getActivity()).pauseTag(getActivity());
//    }
}
