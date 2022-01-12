package com.imooc.entry.dto;

import com.imooc.entry.bo.HeadLine;
import com.imooc.entry.bo.ShopCategory;
import lombok.Data;

import java.util.List;

@Data
public class MainPageInfoDTO {
    private List<HeadLine> headLineList;
    private List<ShopCategory> shopCategorieList;
}
