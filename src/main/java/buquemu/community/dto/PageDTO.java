package buquemu.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class PageDTO {
    private List<QuestionDTO> question;
//    前一页
    private boolean showFirst;
//    《《 最前面
    private boolean showFirstpage;
    //    后一页
    private boolean showNext;
    //    最后面 》》
    private boolean showEndPage;
    private Integer page;
    private Integer totalPage;
    private List<Integer> pages = new ArrayList<>();

    public void setPagination(Integer totalCount,Integer totalPage,Integer page) {
//        高亮前台作比较
        this.page=page;
        this.totalPage=totalPage;
//把当前页插入  。。。。。。。。。。。。。。。。。。。。
        pages.add(page);
//        前三页 后三页
        for (int i = 1; i <=3 ; i++) {
            if(page-i>0){
                pages.add(0,page-i);
            }
            if(page+i<=totalPage){
                pages.add(page+i);
            }
        }
        if (page == 1) {
            showFirst = false;
        } else {
            showFirst = true;
        }
        if (page == totalPage){
            showNext=false;
        }else{
            showNext=true;
        }
//       << 看是否展示第一页
        if(pages.contains(1)){
            showFirstpage=false;
        }else{
            showFirstpage=true;
        }
//        是否展示最后一页  >>
        if(pages.contains(totalPage)){
            showEndPage=false;
        }else{
            showEndPage=true;
        }

    }
}
