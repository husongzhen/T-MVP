package com.ui.article;

import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.widget.ImageView;

import com.C;
import com.app.annotation.apt.Extra;
import com.app.annotation.apt.Router;
import com.app.annotation.apt.SceneTransition;
import com.apt.ApiFactory;
import com.apt.TRouter;
import com.base.BaseActivity;
import com.base.entity.Pointer;
import com.base.util.BindingUtils;
import com.base.util.SpUtil;
import com.base.util.ViewUtil;
import com.google.gson.Gson;
import com.model.Image;
import com.ui.main.R;
import com.ui.main.databinding.ActivityDetailBinding;

@Router(C.ARTICLE)
public class ArticleActivity extends BaseActivity<ArticlePresenter, ActivityDetailBinding> implements ArticleContract.View {
    @Extra(C.HEAD_DATA)
    public Image mArticle;
    @SceneTransition(C.TRANSLATE_VIEW)
    public ImageView image;

    @Override
    protected void initTransitionView() {
        image = mViewBinding.image;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    public void initView() {
        BindingUtils.loadImg(mViewBinding.image, mArticle.image);
        setTitle(mArticle.title);
        String article = new Gson().toJson(new Pointer(Image.class.getSimpleName(), mArticle.objectId));
        mViewBinding.lvComment
                .setHeadView(R.layout.list_item_article, mArticle)
                .setViewType(R.layout.list_item_comment)
                .setIsRefreshable(false);
        mViewBinding.lvComment.getPresenter()
                .setRepository(ApiFactory::getCommentList)
                .setParam(C.INCLUDE, C.CREATER)
                .setParam(C.ARTICLE, article)
                .fetch();
        mViewBinding.btComment.setOnClickListener(v -> {
            String comment = mViewBinding.btComment.getText().toString();
            if (TextUtils.isEmpty(comment))
                Snackbar.make(mViewBinding.fab, "评论不能为空!", Snackbar.LENGTH_LONG).show();
            else {
                mViewBinding.etComment.setText("");
                mPresenter.createComment(comment, mArticle, SpUtil.getUser());
            }
        });
    }

    @Override
    public void commentSuc() {
        mViewBinding.lvComment.reFetch();
        Snackbar.make(mViewBinding.fab, "评论成功!", Snackbar.LENGTH_LONG).show();
        ViewUtil.hideKeyboard(this);
    }

    @Override
    public void commentFail() {
        Snackbar.make(mViewBinding.fab, "评论失败!", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showLoginAction() {
        Snackbar.make(mViewBinding.fab, "请先登录!", Snackbar.LENGTH_LONG)
                .setAction("登录", v -> TRouter.go(C.LOGIN)).show();
    }
}
