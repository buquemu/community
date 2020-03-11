package buquemu.community.controller;

import buquemu.community.Tag.TagCache;
import buquemu.community.dto.TagDTO;
import buquemu.community.model.Question;
import buquemu.community.model.User;
import buquemu.community.dto.QuestionDTO;
import buquemu.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {
    @Autowired
    private QuestionService questionService;

    //编辑 修改
    @PostMapping("/publish/edit")
    public String edit(@RequestParam("id")Integer id
    ,Model model){
        QuestionDTO question = questionService.getById(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id",question.getId());
        model.addAttribute("tagDTO", TagCache.tag());
        return "publish";
    }
//第一次创建的时候
    @GetMapping("/publish")
    public String publish(Model model){
        model.addAttribute("tagDTO", TagCache.tag());
        return "publish";
    }
//创建时返回错误信息
    @PostMapping("/publish")
    public String questionPublish(
            @RequestParam("title")String title,
            @RequestParam("description")String description,
            @RequestParam("tag")String tag,
            @RequestParam("id")Integer id,
            HttpServletRequest request, Model model
            ){
        model.addAttribute("tagDTO", TagCache.tag());
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        if(title==null||title==""){
            model.addAttribute("error","标题不能为空");
            return "publish";

        }
        if(description==null||description==""){
            model.addAttribute("error","问题补充不能为空");
            return "publish";
        }
        if(tag==null||tag==""){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }
//判断标签是否属于标签库
        String s = TagCache.haveTag(tag);
        if(s!=""){
            model.addAttribute("error","输入非法标签"+"'"+s+"'");
            return "publish";
        }

        User user = (User) request.getSession().getAttribute("githubuser");

        if(user==null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setId(id);
        questionService.createOrUpdate(question);
       // return "index";
        return "redirect:/";
    }
}
