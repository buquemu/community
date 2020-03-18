package buquemu.community.service;

import buquemu.community.mapper.PraiseMapper;
import buquemu.community.model.Praise;
import buquemu.community.model.PraiseExample;
import buquemu.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PraiseService {
    @Autowired
    PraiseMapper praiseMapper;
    public int findStatus(Integer commentId, User user) {

        PraiseExample example = new PraiseExample();
        example.createCriteria().andUserEqualTo(user.getId()).andCommentidEqualTo(commentId);
        List<Praise> praises = praiseMapper.selectByExample(example);
//        没有找到
        if(praises.size()==0){
            return 0;
        }else {
            return praises.get(0).getType();
        }
    }
}
