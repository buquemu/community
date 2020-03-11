package buquemu.community.service;

import buquemu.community.dto.NoticeDTO;
import buquemu.community.dto.PageDTO;
import buquemu.community.enums.TongZhiEnum;
import buquemu.community.enums.TongZhiStatusEnum;
import buquemu.community.mapper.NoticeMapper;
import buquemu.community.mapper.UserMapper;
import buquemu.community.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private UserMapper userMapper;
// List里存放的是NoticeDTO
    public PageDTO find(Integer userId, Integer page, Integer size) {
//        List 里放NoticeDTO
        PageDTO<NoticeDTO> pageDTO = new PageDTO<>();
        NoticeExample  noticeExample = new NoticeExample();
//        接收者是当前用户
        noticeExample.createCriteria().andNotofierEqualTo(userId);
        Integer totalCount = (int) noticeMapper.countByExample(noticeExample);
        Integer totalPage = (totalCount % size == 0) ? totalCount / size : totalCount / size + 1;
        //数据校验放置page越界
        if(page<1){
            page=1;
        }
        if(page>totalPage){
            page=totalPage;
        }
//       数据库查询页数
        Integer yeshu = size*(page-1);
        NoticeExample  example = new NoticeExample();
        noticeExample.createCriteria().andNotofierEqualTo(userId);
//        接收了多少条数据
        List<Notice> notices = noticeMapper.selectByExampleWithRowbounds(example,new RowBounds(yeshu,size));
        if(notices.size()==0){
            return pageDTO;
        }
        List<NoticeDTO> noticeDTOS = new ArrayList<>();
        //需要拿到标题和发送人放入noticeDTO
        for (Notice notice : notices) {
            NoticeDTO noticeDTO = new NoticeDTO();
            User user = userMapper.selectByPrimaryKey(notice.getReceiver());
            noticeDTO.setReceiver(user);
            noticeDTO.setOuterId(notice.getOuterid());
            noticeDTO.setDescribe(TongZhiEnum.describe(notice.getType()));
            BeanUtils.copyProperties(notice,noticeDTO);
            noticeDTOS.add(noticeDTO);
        }
        pageDTO.setData(noticeDTOS);
        pageDTO.setPagination(totalPage,page);
        return pageDTO;
    }
//未读
    public int unreadCount(Integer id) {
        NoticeExample example = new NoticeExample();
        example.createCriteria().andNotofierEqualTo(id);
        return (int) noticeMapper.countByExample(example);
    }

// 将未读变成已读
    public void read(int id,int type) {
        NoticeExample notiecExample = new NoticeExample();
        notiecExample.createCriteria().andOuteridEqualTo(id).andTypeEqualTo(type);
//        只能找到唯一的值
        List<Notice> notices = noticeMapper.selectByExample(notiecExample);
        for(Notice notice:notices){

//            outerId question 和 comment 有可能相同  所以要根据type判断
            NoticeExample example = new NoticeExample();
            example.createCriteria().andOuteridEqualTo(id).andTypeEqualTo(type);
            notice.setStatus(TongZhiStatusEnum.READ.getStatus());
            noticeMapper.updateByExampleSelective(notice,example);

        }

    }
}
