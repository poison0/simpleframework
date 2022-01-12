package com.imooc.controller.superadmin;

import com.imooc.entry.bo.HeadLine;
import com.imooc.entry.dto.Result;
import com.imooc.service.solo.HeadLineService;
import org.simpleframework.core.annotation.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
@Controller
public class HeadLineOperationController {
    private HeadLineService headLineService;

    public Result<Boolean> addHeadLine(HttpServletRequest request, HttpServletResponse response) {
        // TODO 校验 转换
        return headLineService.addHeadLine(new HeadLine());
    }
    public Result<Boolean> removeHeadLine(HttpServletRequest request, HttpServletResponse response){
        // TODO 校验 转换
        return headLineService.removeHeadLine(1);
    }
    public Result<Boolean> modifyHeadLine(HttpServletRequest request, HttpServletResponse response){
        // TODO 校验 转换
        return headLineService.modifyHeadLine(new HeadLine());
    }
    public Result<HeadLine> queryHeadLineById(HttpServletRequest request, HttpServletResponse response){
        // TODO 校验 转换
        return headLineService.queryHeadLineById(1);
    }
    public Result<List<HeadLine>> queryHeadLine(HttpServletRequest request, HttpServletResponse response){
        // TODO 校验 转换
        return headLineService.queryHeadLine(new HeadLine(), 1, 100);
    }
}
