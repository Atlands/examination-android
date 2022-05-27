package com.oscar.writtenexamination.Fragment.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leaf.library.StatusBarUtil;
import com.oscar.writtenexamination.Adapter.RecyclerAdapter;
import com.oscar.writtenexamination.Base.Fragment;
import com.oscar.writtenexamination.Bean.CategoryBean;
import com.oscar.writtenexamination.Bean.SubjectBean;
import com.oscar.writtenexamination.R;
import com.oscar.writtenexamination.Utils.ToastUtils;
import com.oscar.writtenexamination.Utils.UiUtils;
import com.oscar.writtenexamination.Widget.PageIndicatorView;
import com.oscar.writtenexamination.Widget.PageRecyclerView;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import static android.view.View.VISIBLE;
import static com.oscar.writtenexamination.Base.Configurations.CATEGORY_CLASS_NAME;
import static com.oscar.writtenexamination.Base.Configurations.CATEGORY_GOOGLE_ORIGINAL_PRICE;
import static com.oscar.writtenexamination.Base.Configurations.CATEGORY_GOOGLE_SPECIAL_PRICE;
import static com.oscar.writtenexamination.Base.Configurations.CATEGORY_IMG;
import static com.oscar.writtenexamination.Base.Configurations.CATEGORY_INFO;
import static com.oscar.writtenexamination.Base.Configurations.CATEGORY_IS_HIDE;
import static com.oscar.writtenexamination.Base.Configurations.CATEGORY_NAME;
import static com.oscar.writtenexamination.Base.Configurations.CATEGORY_POINTER;
import static com.oscar.writtenexamination.Base.Configurations.CONFIG_HOME_NAME;
import static com.oscar.writtenexamination.Base.Configurations.CREATED_AT;
import static com.oscar.writtenexamination.Base.Configurations.GET_PRODUCTS;
import static com.oscar.writtenexamination.Base.Configurations.PURCHASED_HISTORY_CLASS_NAME;
import static com.oscar.writtenexamination.Base.Configurations.SCROLL_AD_CLASS_NAME;
import static com.oscar.writtenexamination.Base.Configurations.SCROLL_AD_IMAGE;
import static com.oscar.writtenexamination.Base.Configurations.SCROLL_AD_LINK;
import static com.oscar.writtenexamination.Base.Configurations.SCROLL_AD_TITLE;
import static com.oscar.writtenexamination.Base.Configurations.USER_POINTER;
import static com.oscar.writtenexamination.Base.Configurations.configOoject;
import static com.oscar.writtenexamination.Base.Configurations.getParseImage;
import static com.oscar.writtenexamination.Base.Configurations.getTopNewsContent;
import static com.oscar.writtenexamination.Base.Configurations.goCategory;
import static com.oscar.writtenexamination.Base.Configurations.isLogin;
import static com.oscar.writtenexamination.Base.Configurations.isNetConnect;
import static com.oscar.writtenexamination.Base.Configurations.judgeEmptyList;
import static com.oscar.writtenexamination.Base.Configurations.setText;

public class HomeFragment extends Fragment implements OnBannerListener {

    //Toolbar
    @BindView(R.id.fhToolbar)
    Toolbar mToolbar;
    @BindView(R.id.fhTitleTv)
    TextView tvTitle;

    //公告栏
    @BindView(R.id.fhNewsLayout)
    LinearLayout llNews;
    @BindView(R.id.fhNewsVF)
    ViewFlipper viewFlipper;
    @BindView(R.id.fhNewsLoading)
    TextView tvNewsLoading;
    //轮播广告
    @BindView(R.id.fhPBLayout)
    RelativeLayout pbLayout;
    @BindView(R.id.fhProgressBar)
    ProgressBar progressBar;
    @BindView(R.id.fhBanner)
    Banner topBanner;
    //热门考试
    @BindView(R.id.fhHotCateLayout)
    LinearLayout llHoteCate;
    PageRecyclerView hotRy;
    @BindView(R.id.fhHCPIV)
    PageIndicatorView hotIV;
    //考试类别
    @BindView(R.id.fhCMoreBtn)
    TextView btnCateMore;
    @BindView(R.id.fhCateRy)
    RecyclerView cateRy;

    //参数
    int spanRow = 3;
    int spanColumn = 1;
    private boolean isLoadCategory = false;
    //适配器
    RecyclerAdapter<CategoryBean> mAdapter;
    PageRecyclerView.PageAdapter hotAdapter;

    private List<CategoryBean> categoryList = new ArrayList<>();
    private List<ParseObject> hotCateList = new ArrayList<>();

    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_home;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        StatusBarUtil.setGradientColor(mActivity, mToolbar);
        UiUtils.setStatusBarDarkTheme(mActivity, true);
        //初始化公告
        getTopNewsContent(mContext,tvNewsLoading,viewFlipper);
        //初始化广告图
        setBanner();
    }


    @Override
    protected void initData() {
        if (!isNetConnect) {
            ToastUtils.showShortToast(mContext, R.string.tipsNetworkConnectFailedPlzCheckIt);
            return;
        }
        //数据
        String homeName;
        if (configOoject == null){
            homeName = AppUtils.getAppName();
        }else {
            homeName = configOoject.getString(CONFIG_HOME_NAME);
        }
        setText(tvTitle, homeName);
        initHotCate();

        queryCategory();
    }

    /**
     * 初始化热门考试
     */
    private void initHotCate() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CATEGORY_CLASS_NAME);
        query.whereEqualTo(CATEGORY_IS_HIDE,false);
        query.orderByDescending(CREATED_AT);
        query.findInBackground((objects, e) -> {
            if (e == null){
                boolean isEmpty = judgeEmptyList(objects);
                llHoteCate.setVisibility(isEmpty ? View.GONE : VISIBLE);
                if (!isEmpty){
                    if (objects.size() <= 12){
                        hotCateList = objects;
                    }else {
                        hotCateList.clear();
                        for (int i=0;i < 12;i++){
                            hotCateList.add(objects.get(i));
                        }
                    }
                    hotRy = mRoot.findViewById(R.id.fhHCPRY);
                    //设置指示器
                    hotRy.setIndicator(hotIV);
                    //设置行列数
                    int size = hotCateList.size();
                    if (size / 4 > 2 && size % 4 == 0){
                        spanRow = 4;
                    }else {
                        if (size / 3 > 1 && size % 3 > 1){
                            spanRow = 3;
                        }else {
                            spanRow = 2;
                        }
                    }
                    hotRy.setPageSize(spanRow,spanColumn);
                    //设置页间距
                    hotRy.setPageMargin(30);
                    //设置适配器
                    hotRy.setAdapter(hotAdapter = hotRy.new PageAdapter(hotCateList, new PageRecyclerView.CallBack() {
                        @Override
                        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(mActivity).inflate(R.layout.item_category_hot,parent,false);
                            return new HotCateHolder(view);
                        }

                        @Override
                        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
                            ParseObject cObj = hotCateList.get(position);
                            HotCateHolder holder = (HotCateHolder) viewHolder;
                            //基础资料
                            String name = cObj.getString(CATEGORY_NAME);
                            setText(holder.tvName,name);
                            String infor = cObj.getString(CATEGORY_INFO);
                            setText(holder.tvInfor,infor);
                            //设置图片
                            getParseImage(holder.cImg,cObj,CATEGORY_IMG);

                            //价格
                            int oPrice = cObj.getInt(CATEGORY_GOOGLE_ORIGINAL_PRICE);
                            int sPrice = cObj.getInt(CATEGORY_GOOGLE_SPECIAL_PRICE);
                            boolean isSpecial = sPrice == 0;
                            setText(holder.tvOPrice,"$" + oPrice);
                            if (!isSpecial){
                                setText(holder.tvSPrice,"$" + sPrice);
                                holder.tvOPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG ); //中间横线
                                holder.tvOPrice.setTextColor(ColorUtils.getColor(R.color.colorBF));
                                holder.tvSPrice.setVisibility(VISIBLE);
                                holder.staSpecial.setVisibility(VISIBLE);
                            }
                            //是否购买,暂时只判断category
                            if (isLogin()){
                                ParseQuery<ParseObject> query1 = ParseQuery.getQuery(PURCHASED_HISTORY_CLASS_NAME);
                                query1.whereEqualTo(USER_POINTER, ParseUser.getCurrentUser());
                                query1.whereEqualTo(CATEGORY_POINTER,cObj);
                                query1.getFirstInBackground((object, e1) -> setPurchased(holder,e1 == null));
                            }else {
                                setPurchased(holder,false);
                            }

                            //点击事件
                            holder.llItem.setOnClickListener(view -> {
                                goCategory(mContext,cObj);
                            });
                        }
                    }));
                    hotRy.startTime();
                }
            }else {
                ToastUtils.showLongToast(mContext,getString(R.string.tipsGetHoteCateFailedPlzRetry)
                        + "：" + e.getMessage());
            }
        });
    }

    /**
     * 免费试用和已购买
     */
    private void setPurchased(HotCateHolder holder,boolean isPur){
        holder.staPurchased.setText(mContext.getString(isPur ? R.string.tipsPurchased : R.string.tipsFreeTrial));
        holder.staPurchased.setBackgroundColor(ColorUtils.getColor(isPur ? R.color.colorRedNormal : R.color.colorBlack95));
    }


    /**
     * 加载考试类别
     */
    private void queryCategory() {
        HashMap<String,Object> params = new HashMap<>();
        ParseCloud.callFunctionInBackground(GET_PRODUCTS, params, (FunctionCallback<List<Map<String,Object>>>) (datas, e) -> {
            if (e == null){
                isLoadCategory = !judgeEmptyList(datas);
                if (isLoadCategory){
                    Gson gson = new Gson();
                    String jsonStr = gson.toJson(datas);
                    categoryList = gson.fromJson(jsonStr, new TypeToken<List<CategoryBean>>(){}.getType());
                    initCategory();
                }
            }else {
                isLoadCategory = false;
            }
            btnCateMore.setActivated(isLoadCategory);
        });
    }

    /**
     * 初始化category
     */
    private void initCategory(){
        //布局参数
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cateRy.setLayoutManager(layoutManager);
        cateRy.setHasFixedSize(true);
        cateRy.setNestedScrollingEnabled(false);
        if (mAdapter == null){
            mAdapter = new RecyclerAdapter<CategoryBean>(categoryList,null) {
                @Override
                protected int getItemViewType(int position, CategoryBean categoryBean) {
                    return R.layout.item_category_normal;
                }

                @Override
                protected ViewHolder<CategoryBean> onCreateViewHolder(View root, int viewType) {
                    return new CategoryHolder(root,mContext);
                }
            };
            mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<CategoryBean>() {
                @Override
                public void onItemClick(RecyclerAdapter.ViewHolder holder, CategoryBean categoryBean) {
                    goCategory(mContext,categoryBean);
                }
            });
            cateRy.setAdapter(mAdapter);

        }else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void setNetConnect(boolean isNetConnect) {

    }

    /**
     * 设置轮播图广告
     */
    private void setBanner() {
        pbLayout.setBackgroundColor(ColorUtils.getColor(R.color.colorBlack95));
        topBanner.setVisibility(View.GONE);
        progressBar.setVisibility(VISIBLE);
        List<String> bannerList = new ArrayList<>();
        List<String> titleList = new ArrayList<>();
        List<String> linkList = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(SCROLL_AD_CLASS_NAME);
        query.findInBackground((objects, e) -> {
            if (e == null){
                if (objects.size() == 0){
                    ToastUtils.showBlackCenterToast(mContext,getString(R.string.tipsGetBannerFailed));
                }else {
                    for (ParseObject object : objects){
                        ParseFile parseFile = object.getParseFile(SCROLL_AD_IMAGE);
                        if (parseFile != null){
                            bannerList.add(parseFile.getUrl());
                            String title = object.getString(SCROLL_AD_TITLE);
                            titleList.add(TextUtils.isDigitsOnly(title) ? getString(R.string.tipsOfficalAd) : title);
                            String link = object.getString(SCROLL_AD_LINK);
                            linkList.add(TextUtils.isEmpty(link) ? "" : link);
                        }
                    }
                    if (bannerList.size() > 0){
                        pbLayout.setBackgroundColor(ColorUtils.getColor(R.color.colorWhite));
                        progressBar.setVisibility(View.GONE);
                        topBanner.setVisibility(VISIBLE);
                        //设置内置样式，共有六种可以
                        topBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
                        //设置图片加载器，图片加载器在下方
                        topBanner.setImageLoader(new MyLoader());
                        //设置图片网址或地址的集合
                        topBanner.setImages(bannerList);
                        //设置图片标题
                        topBanner.setBannerTitles(titleList);
                        //设置轮播的动画效果，内含多种特效
                        topBanner.setBannerAnimation(Transformer.Default);
                        //设置轮播间隔时间
                        topBanner.setDelayTime(3000);
                        //设置是否为自动轮播
                        topBanner.isAutoPlay(true);
                        //设置指示器的位置，圆点，左中右。
                        topBanner.setIndicatorGravity(BannerConfig.CENTER);
                        topBanner.setOnBannerListener(this);
                        topBanner.start();
                        topBanner.setOnBannerListener(position -> {
                            //设置广告点击事件（链接跳转）
                            if (!judgeEmptyList(linkList)){
                                String link = linkList.get(position);
                                if (!TextUtils.isEmpty(link)){
                                    ToastUtils.showBlackCenterToast(mContext,link);
                                }
                            }
                        });
                    }
                }
            }else {
                ToastUtils.showShortToast(mContext,getString(R.string.tipsGetBannerFailed)
                        + "：" + e.getMessage());
            }
        });
    }

    @Override
    public void OnBannerClick(int position) {

    }

    private static class HotCateHolder extends RecyclerView.ViewHolder {

        ImageView cImg;
        TextView tvName,tvInfor,tvOPrice,tvSPrice;
        TextView staPurchased,staSpecial;
        LinearLayout llItem,llPrice;

        HotCateHolder(@NonNull View item) {
            super(item);

            cImg = item.findViewById(R.id.ichMainImg);
            tvName = item.findViewById(R.id.ichNameTv);
            tvInfor = item.findViewById(R.id.ichInforTv);
            tvOPrice = item.findViewById(R.id.ichOriginalPriceTv);
            tvSPrice = item.findViewById(R.id.ichSpecialPriceTv);
            staPurchased = item.findViewById(R.id.ichPurchhasedStatus);
            staSpecial = item.findViewById(R.id.ichSpecialStatus);
            llPrice = item.findViewById(R.id.ichPriceLayout);
            llItem = item.findViewById(R.id.ichItem);

        }
    }

    /**
     * 科目表ViewHolder
     */
    static class CategoryHolder extends RecyclerAdapter.ViewHolder<CategoryBean>{
        @BindView(R.id.icnMainImg)
        ImageView imgMain;
        @BindView(R.id.icnPurchasedStatus)
        TextView tvPurStatus;

        @BindView(R.id.icnNameTv)
        TextView tvName;
        @BindView(R.id.icnInforTv)
        TextView tvInfor;

        @BindView(R.id.icnSubNumTv)
        TextView tvSubNum;
        @BindView(R.id.icnOriginalPriceTv)
        TextView tvOPrice;
        @BindView(R.id.icnSpecialPriceTv)
        TextView tvSPrice;
        @BindView(R.id.icnSpecialStatus)
        TextView tvSpeStatus;
        @BindView(R.id.icnPurchasedNumTv)
        TextView tvPurNum;

        private Context context;

        CategoryHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onBind(CategoryBean categoryBean) {

            //左侧
            getParseImage(imgMain, categoryBean.getCategoryImgUrl());
            boolean isBuy = categoryBean.isBuy();
            setPurchased(isBuy);
            //基础
            String name = categoryBean.getCategoryName();
            setText(tvName,name);
            String infor = categoryBean.getCategoryInfo();
            setText(tvInfor,infor);
            //科目
            List<SubjectBean> subjectBeans = categoryBean.getSubjects();
            setSubjectNum(subjectBeans);
            //价格
            float oPrice = categoryBean.getGoogleOriginalPrice();
            float sPrice = categoryBean.getGoogleSpecialPrice();
            boolean isSpecial = sPrice > 0;
            setText(tvOPrice,"$" + oPrice);
            tvSpeStatus.setVisibility(View.GONE);
            tvSPrice.setVisibility(View.GONE);
            if (isSpecial){
                setText(tvSPrice,"$" + sPrice);
                tvOPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG ); //中间横线
                tvOPrice.setTextColor(ColorUtils.getColor(R.color.colorBF));
                tvSPrice.setVisibility(View.VISIBLE);
                tvSpeStatus.setVisibility(View.VISIBLE);
            }

        }

        @Override
        protected void onBind(CategoryBean categoryBean, List<Object> payloads) {

        }

        /**
         * 免费试用和已购买
         */
        private void setPurchased(boolean isPurchased){
            tvPurStatus.setText(context.getString(isPurchased ? R.string.tipsPurchased : R.string.tipsFreeTrial));
            tvPurStatus.setBackgroundColor(ColorUtils.getColor(isPurchased ? R.color.colorRedNormal : R.color.colorBlack95));
        }

        /**
         * 设置科目数
         */
        private void setSubjectNum(List<SubjectBean> subjectBeans) {
            boolean isEmpty = judgeEmptyList(subjectBeans);
            tvSubNum.setVisibility(isEmpty ? View.GONE : VISIBLE);
            if (!isEmpty){
                setText(tvSubNum,subjectBeans.size() + "門科目");
            }
        }
    }

    private static class MyLoader extends ImageLoader{

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        topBanner.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        topBanner.stopAutoPlay();
    }

}
