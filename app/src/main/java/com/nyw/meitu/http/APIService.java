package com.nyw.meitu.http;



import com.nyw.meitu.model.GirlResult;
import com.nyw.meitu.model.GirlResult2;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * @since 2016/1/11
 */
public interface APIService {


    /**
     * http://gank.io/api/data/福利/10/1
     * @param type 可选参数: Android | iOS | 休息视频 | 福利 | 拓展资源 | 前端 | 瞎推荐 | App
     * @param count
     * @param page
     * @return
     */
    @GET("api/data/{type}/{count}/{page}")
    Observable<GirlResult> getGirs(@Path("type") String type, @Path("count") int count, @Path("page") int page);
    /*
    * URL：http://image.baidu.com/data/imgs?col=&tag=&sort=&pn=&rn=&p=channel&from=1
    *参数：col=大类&tag=分类&sort=0&pn=开始条数&rn=显示数量&p=channel&from=1
    *PS：sort可以为0和1，作用。。未知
    * */
    @GET("data/imgs?&from=1&sort=0&p=channel")
    Observable<GirlResult2> getGirs2(@Query("col") String type, @Query("tag") String ftags, @Query("pn") int num, @Query("rn") int page);
}
