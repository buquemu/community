package buquemu.community.mapper;

import buquemu.community.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Insert("insert into user (name,account_id,token,gmt_Create,gmt_modified,avatar_url) values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);
    @Select("select * from user where token = #{token}")
    User findBycookie(@Param("token") String token);


    @Select("select * from user where name = #{name}")
    User findById(@Param("name") String creator);

    @Select("select * from user where account_id = #{accountId}")
    User findByAccounId(@Param("accountId") String accountId);
    @Update("update user set name = #{name} ,gmt_modified=#{gmtModified},token=#{token},avatar_url=#{avatarUrl} where account_id = #{accountId}")
    void upDate(User user);
}
