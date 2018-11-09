package com.tm.wechat.dao.sign;

import com.tm.wechat.domain.VideoSignAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by pengchao on 2018/8/28.
 */
public interface VideoSignAccountRepository extends JpaRepository<VideoSignAccount, String> {
    VideoSignAccount findTop1BySystemUserName(String userName);
    VideoSignAccount findTop1BySignAccount(String signAccount);
    List<VideoSignAccount> findBySystemUserNameOrderByUpdateTimeAsc(String userName);
}
