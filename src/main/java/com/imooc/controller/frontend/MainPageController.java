package com.imooc.controller.frontend;

import com.imooc.entry.dto.MainPageInfoDTO;
import com.imooc.entry.dto.Result;
import com.imooc.service.combine.HeadLineShopCategoryCombineService;
import org.simpleframework.core.annotation.Controller;
import org.simpleframework.inject.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MainPageController {
    @Autowired("HeadLineShopCategoryCombineService2Impl")
    private HeadLineShopCategoryCombineService headLineShopCategoryCombineService;

    public Result<MainPageInfoDTO> getMainPageInfo(HttpServletRequest request, HttpServletResponse response) {
        return headLineShopCategoryCombineService.getMainPageInfo();
    }
}
