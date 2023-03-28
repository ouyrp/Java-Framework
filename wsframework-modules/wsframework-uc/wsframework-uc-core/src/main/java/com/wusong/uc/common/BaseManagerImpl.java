package com.wusong.uc.common;

import com.galaxy.ws.spec.common.core.domain.RestResult;
import com.wusong.web.dto.ApiResult;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Response;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseManagerImpl {

    protected <T> ApiResult<T> execute(Call<ApiResult<T>> call) {
        Response<ApiResult<T>> response;
        try {
            response = call.execute();
        } catch (Exception e) {
            log.error("execute error,url:{}", call.request().url().url(), e);
            return ApiResult.error(-1, "访问失败");
        }
        if (response.isSuccessful()) {
            return response.body();
        } else {
            return ApiResult.error(response.code(), "请求失败:" + call.request().url().url());
        }
    }

    protected <T> RestResult<T> executeRestResult(Call<RestResult<T>> call) {
        Response<RestResult<T>> response;
        try {
            response = call.execute();
        } catch (Exception e) {
            log.error("execute error,url:{}", call.request().url().url(), e);
            return RestResult.of(-1, "访问失败");
        }
        if (response.isSuccessful()) {
            return response.body();
        } else {
            return RestResult.of(response.code(), "请求失败:" + call.request().url().url());
        }
    }
}
